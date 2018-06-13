package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.eventbus.EventBus;
import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.config.Configuration;
import com.shbaoyuantech.commons.CompanyType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CanalInstance implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CanalInstance.class);

    private static Configuration config;

    private String canalDestination;

    private String dateBase;

    public CanalInstance(String canalDestination, String dateBase) {
        this.canalDestination = canalDestination;
        this.dateBase = dateBase;
    }

    /**
     * 主方法：建立canal连接，监听数据是否变化
     *
     * @throws Exception
     * @author liu
     */
    private void process() throws Exception {
        config = Configuration.getInstance();

        if (StringUtils.isEmpty(this.canalDestination) || StringUtils.isEmpty(this.dateBase)) {
            throw new RuntimeException("error##CanalInstance::process, canalDestination or dataBase can not be null!");
        }

        //建立与canal服务器的连接
        CanalConnector connector = null;
        try {
            connector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress(config.getCanalHost(), config.getCanalPort()),
                    this.canalDestination,
                    config.getCanalUsername(), config.getCanalPassword());

            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();

            logger.info("CanalInstance::process, cnhutong binlog process starting ...");

            while (true) {
                Message message = connector.getWithoutAck(config.getBatchSize());
                long batchId = message.getId();
                if (batchId == -1 || message.getEntries().isEmpty()) {
                    logger.info("CanalInstance::process, no change in the data, wait a minute ...");
                    Thread.sleep(config.getInterval());
                    continue;
                }

                parseEntries(message.getEntries());
                connector.ack(batchId);

                resetRetryTimes(Thread.currentThread());
                //todo thread 挂了 能否拿到handler
            }
        } finally {
            if (connector != null) {
                connector.disconnect();
            }
        }
    }

    private void resetRetryTimes(Thread thread){
        EventBus eventBus = new EventBus();
        eventBus.register(new ByCanalApplication());
        eventBus.post(thread);
    }

    /**
     * 解析entries
     *
     * @param entries
     * @anthor liu
     */
    private void parseEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            if (isTransactionOperation(entry) || isDatabaseIgnored(entry) || isTableIgnored(entry)) {
                continue;
            }
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("error##CanalInstance::parseEntries, parsing row value has an error, data:" + entry.toString(), e);
            }

            EventType eventType = rowChange.getEventType();
            if (eventType == EventType.QUERY) {
                continue;
            }

            if (eventType == EventType.DELETE) {
                throw new RuntimeException("error##CanalInstance::parseEntries, delete operation is illegal." + entry.toString());
            }

            logger.debug(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            int companyId = dateBase.contains("guowen") ? CompanyType.GUOWEN : (dateBase.contains("guoyi") ? CompanyType.GUOYI : CompanyType.COMMON);
            DataSynchronize.synchronizeData(rowChange, entry, companyId);
        }
    }

    private static boolean isTransactionOperation(Entry entry) {
        return entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND;
    }

    private boolean isDatabaseIgnored(Entry entry) {
        String currentDataBase = this.dateBase;
        return !currentDataBase.equalsIgnoreCase(entry.getHeader().getSchemaName());
    }

    private static boolean isTableIgnored(Entry entry) {
        Pattern p = Pattern.compile(config.getTables());
        Matcher m = p.matcher(entry.getHeader().getTableName());
        return !m.matches();
    }

    @Override
    public void run() {
        try {
            process();
        } catch (BizException e){
            logger.error("bizException::", e);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

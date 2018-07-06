package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;
import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.config.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shbaoyuantech.commons.Constants.*;

public class CanalInstance implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CanalInstance.class);

    private static Configuration config = Configuration.getInstance();

    private String canalDestination;
    private String database;
    private Status status;

    public CanalInstance(String canalDestination, String database) {
        this.canalDestination = canalDestination;
        this.database = database;
    }

    @Override
    public void run() {
        try {
            status = Status.INIT;
            process();
        } catch (BizException e) {
            logger.error("bizException::", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void process() throws Exception {
        if (StringUtils.isEmpty(canalDestination) || StringUtils.isEmpty(database)) {
            throw new RuntimeException("error##CanalInstance::process, canalDestination or database can not be null!");
        }

        CanalConnector connector = null;
        try {
            connector = CanalConnectors.newSingleConnector(
                    new InetSocketAddress(config.getCanalHost(), config.getCanalPort()),
                    canalDestination,
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

                if (status == Status.INIT) {
                    ExceptionHandler handler = (ExceptionHandler) Thread.currentThread().getUncaughtExceptionHandler();
                    handler.resetMaxRetryTimes();
                    status = Status.PROCESSING;
                }
            }
        } finally {
            if (connector != null) {
                try {
                    connector.disconnect();
                } catch (Exception e) {
                    logger.error("Internal Error:", e);
                }
            }
            status = Status.FINISHED;
        }
    }

    private void parseEntries(List<Entry> entries) {
        entries.forEach(entry -> {
            if (isTransactionOperation(entry) || isDatabaseIgnored(entry) || isTableIgnored(entry)) {
                return;
            }
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("error##CanalInstance::parseEntries, parsing row value has an error, data:" + entry.toString(), e);
            }

            EventType eventType = rowChange.getEventType();
            if (eventType == EventType.QUERY) {
                return;
            }

            if (eventType == EventType.DELETE) {
                // TODO: sync to delete data
                logger.error("error##CanalInstance::parseEntries, delete operation is illegal." + entry.toString());
                return;
            }

            logger.debug(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            int dbCode = database.contains("guowen") ? DB_CODE_GUOWEN : (database.contains("guoyi") ? DB_CODE_GUOYI : DB_CODE_COMMON);
            new DataSyncWorker(rowChange, entry, dbCode).act();
        });
    }

    private static boolean isTransactionOperation(Entry entry) {
        return entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND;
    }

    private boolean isDatabaseIgnored(Entry entry) {
        return !database.equalsIgnoreCase(entry.getHeader().getSchemaName());
    }

    private static boolean isTableIgnored(Entry entry) {
        Pattern p = Pattern.compile(config.getTables());
        Matcher m = p.matcher(entry.getHeader().getTableName());
        return !m.matches();
    }

    private enum Status {
        INIT, PROCESSING, FINISHED
    }
}

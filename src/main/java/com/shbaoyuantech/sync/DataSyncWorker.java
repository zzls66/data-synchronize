package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.shbaoyuantech.commons.MongoUtils;
import com.shbaoyuantech.config.BeanConfiguration;
import com.shbaoyuantech.config.BeanMetaInfo;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

import static com.shbaoyuantech.commons.Constants.ROW_ID;

public class DataSyncWorker {

    private RowChange rowChange;
    private int dbCode;

    private String table;
    private BeanMetaInfo beanMetaInfo;
    private String collection;
    private CanalEntry.EventType eventType;
    private FieldHandler fieldHandler;

    public DataSyncWorker(RowChange rowChange, Entry entry, int dbCode) {
        this.rowChange = rowChange;
        this.dbCode = dbCode;

        table = entry.getHeader().getTableName();
        beanMetaInfo = BeanConfiguration.getBeanMetaInfo(table);
        if (beanMetaInfo != null) {
            collection = beanMetaInfo.getCollection();
        }
        eventType = rowChange.getEventType();
        fieldHandler = new FieldHandler(beanMetaInfo, dbCode, eventType);
    }

    public void act() {
        if (StringUtils.isEmpty(collection)) {
            return;
        }

        rowChange.getRowDatasList().forEach(rowData -> {
            Map<BeanMetaInfo.BeanField, String> fieldsWithValue = extractBeanFieldsFromRowColumns(rowData);
            Map<String, Object> data = fieldHandler.handle(fieldsWithValue);

            if (eventType == CanalEntry.EventType.INSERT) {
                insertOne(data);
            }

            if (eventType == CanalEntry.EventType.UPDATE) {
                updateOne(data);
            }
        });
    }

    private Map<BeanMetaInfo.BeanField, String> extractBeanFieldsFromRowColumns(CanalEntry.RowData rowData) {
        Map<BeanMetaInfo.BeanField, String> fields = new HashMap<>();
        rowData.getAfterColumnsList().forEach(column -> {
            boolean needToSync = column.getUpdated() && beanMetaInfo.isColumnExistingInFields(column.getName());
            boolean isId = column.getName().equals("id");
            if (needToSync || isId) {
                BeanMetaInfo.BeanField beanField = beanMetaInfo.getBeanField(column.getName());
                fields.put(beanField, column.getValue());
            }
        });

        return fields;
    }

    private void insertOne(Map<String, Object> data) {
        Document doc = MongoUtils.findOneBy(collection, buildIdentifierFilter(data.get(ROW_ID)));
        if (doc != null) {
            return;
        }
        MongoUtils.insertOne(collection, data);
    }

    private void updateOne(Map<String, Object> data) {
        MongoUtils.updateOne(collection, buildIdentifierFilter(data.get(ROW_ID)), data);
    }

    private Map<String, Object> buildIdentifierFilter(Object rowId) {
        return MongoUtils.buildIdentifierFilter((int) rowId, dbCode);
    }
}

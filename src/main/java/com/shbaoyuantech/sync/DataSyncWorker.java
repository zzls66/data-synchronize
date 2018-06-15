package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.google.common.collect.ImmutableMap;
import com.shbaoyuantech.commons.MongoUtils;
import com.shbaoyuantech.config.BeanConfiguration;
import com.shbaoyuantech.config.BeanMetaInfo;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shbaoyuantech.commons.Constants.DB_CODE;
import static com.shbaoyuantech.commons.Constants.ROW_ID;

public class DataSyncWorker {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private RowChange rowChange;
    private int dbCode;
    private String table;
    private BeanMetaInfo beanMetaInfo;
    private String collection;
    private CanalEntry.EventType eventType;

    public DataSyncWorker(RowChange rowChange, Entry entry, int dbCode) {
        this.rowChange = rowChange;
        this.dbCode = dbCode;
        this.table = entry.getHeader().getTableName();
        this.beanMetaInfo = BeanConfiguration.getBeanMetaInfo(table);
        this.collection = beanMetaInfo.getCollection();
        this.eventType = rowChange.getEventType();
    }

    public void act() {
        if (StringUtils.isEmpty(collection)) {
            return;
        }

        rowChange.getRowDatasList().forEach(rowData -> {
            Map<String, Object> data = extractDataFromRowColumns(rowData);

            handleRefDateFields(data);
            handleSupplementalFieldRoles(data);

            if (eventType == CanalEntry.EventType.INSERT) {
                insertOne(data);
            }

            if (eventType == CanalEntry.EventType.UPDATE) {
                updateOne(data);
            }
        });
    }

    private Map<String, Object> extractDataFromRowColumns(CanalEntry.RowData rowData) {
        Map<String, Object> data = new HashMap<String, Object>(){{
            put(DB_CODE, dbCode);
        }};
        rowData.getAfterColumnsList().forEach(column -> {
            boolean needToSync = column.getUpdated() && beanMetaInfo.isColumnExistingInFields(column.getName());
            boolean isId = column.getName().equals("id");
            if (needToSync || isId) {
                BeanMetaInfo.BeanField beanField = beanMetaInfo.getBeanField(column.getName());
                data.put(beanField.getField(), convertValue(beanField, column.getValue()));
            }
        });

        return data;
    }

    private Object convertValue(BeanMetaInfo.BeanField beanField, String value) {
        if (value == null) {
            return null;
        }

        Class fieldType = beanField.getFieldType();

        if (fieldType.equals(String.class)) {
            return value;
        }
        if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return Integer.parseInt(value) > 0;
        }
        if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
            return Double.parseDouble(value);
        }
        if (fieldType.equals(Date.class)) {
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                return null;
            }
        }
        if (fieldType.equals(ObjectId.class)) {
            String collection = beanField.getRefCollection();
            Document doc = MongoUtils.findOneBy(collection, buildIdentifierFilter(Integer.parseInt(value)));
            if (doc != null) {
                return doc.getObjectId("_id");
            }
        }

        return null;
    }

    private void handleRefDateFields(Map<String, Object> data) {
        beanMetaInfo.getFields().stream().filter(beanField -> {
            String refDateField = beanField.getRefDateField();
            return beanField.isSupplemental() && !StringUtils.isEmpty(refDateField) && data.containsKey(refDateField);
        }).forEach(beanField -> {
            Date refDate = (Date) data.get(beanField.getRefDateField());
            if (refDate == null) {
                data.put(beanField.getField(), null);
                return;
            }
            ObjectId dateObjectId = MongoUtils.findDateObjectId(refDate);
            data.put(beanField.getField(), dateObjectId);
        });
    }

    private void handleSupplementalFieldRoles(Map<String, Object> data) {
        if(!"by_lead_history".equals(table) || eventType != CanalEntry.EventType.INSERT) {
            return;
        }

        ObjectId operatorId = (ObjectId) data.get("operator_id");
        List<Document> docs = MongoUtils.findManyBy("dim_staff_positions", ImmutableMap.of("staff_id", operatorId));

        Set<Integer> roles = new HashSet<>();
        docs.forEach(doc -> {
            ObjectId dutyObjectId = doc.getObjectId("duty_id");
            Document duty = MongoUtils.findOneBy("dim_duty", ImmutableMap.of("_id", dutyObjectId));
            if (duty != null) {
                roles.add(duty.getInteger(ROW_ID));
            }
        });
        data.put("roles", roles);
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
        return ImmutableMap.of(ROW_ID, rowId, DB_CODE, dbCode);
    }
}

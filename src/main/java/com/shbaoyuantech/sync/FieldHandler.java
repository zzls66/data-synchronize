package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.ImmutableMap;
import com.shbaoyuantech.commons.MongoUtils;
import com.shbaoyuantech.config.BeanMetaInfo;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.shbaoyuantech.commons.Constants.DB_CODE;
import static com.shbaoyuantech.commons.Constants.ROW_ID;

public class FieldHandler {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int dbCode;
    private BeanMetaInfo beanMetaInfo;
    private CanalEntry.EventType eventType;

    public FieldHandler(BeanMetaInfo beanMetaInfo, int dbCode, CanalEntry.EventType eventType) {
        this.dbCode = dbCode;
        this.beanMetaInfo = beanMetaInfo;
        this.eventType = eventType;
    }

    public Map<String, Object> handle(Map<BeanMetaInfo.BeanField, String> fieldsWithValue) {
        Map<String, Object> data = new HashMap<String, Object>(){{
            put(DB_CODE, dbCode);
        }};
        fieldsWithValue.forEach((beanField, value) -> data.put(beanField.getField(), convertValue(beanField, value)));

        handleRefDateFields(data);
        handleSupplementalFieldRoles(data);

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
            Document doc = MongoUtils.findOneBy(collection, MongoUtils.buildIdentifierFilter(Integer.parseInt(value), dbCode));
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
        if(!"by_lead_history".equals(beanMetaInfo.getTable()) || eventType != CanalEntry.EventType.INSERT) {
            return;
        }

        ObjectId operatorId = (ObjectId) data.get("operator_id");
        List<Document> docs = MongoUtils.findManyBy("dim_staff_positions", ImmutableMap.of("staff_id", operatorId));

        // TODO: load duties in one query
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
}

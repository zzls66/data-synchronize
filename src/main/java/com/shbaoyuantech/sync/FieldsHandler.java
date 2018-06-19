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

public class FieldsHandler {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int dbCode;
    private BeanMetaInfo beanMetaInfo;
    private CanalEntry.EventType eventType;

    public FieldsHandler(BeanMetaInfo beanMetaInfo, int dbCode, CanalEntry.EventType eventType) {
        this.dbCode = dbCode;
        this.beanMetaInfo = beanMetaInfo;
        this.eventType = eventType;
    }

    public Document handle(Map<BeanMetaInfo.BeanField, String> fieldsWithValue) {
        Document doc = new Document();
        doc.append(DB_CODE, dbCode);
        fieldsWithValue.forEach((beanField, value) -> doc.append(beanField.getField(), convertValue(beanField, value)));

        handleRefDateFields(doc);
        handleSupplementalFieldRoles(doc);

        return doc;
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

    private void handleRefDateFields(Document doc) {
        beanMetaInfo.getFields().stream().filter(beanField -> {
            String refDateField = beanField.getRefDateField();
            return beanField.isSupplemental() && !StringUtils.isEmpty(refDateField) && doc.containsKey(refDateField);
        }).forEach(beanField -> {
            Date refDate = (Date) doc.get(beanField.getRefDateField());
            if (refDate == null) {
                doc.append(beanField.getField(), null);
                return;
            }
            ObjectId dateObjectId = MongoUtils.findDateObjectId(refDate);
            doc.append(beanField.getField(), dateObjectId);
        });
    }

    private void handleSupplementalFieldRoles(Document doc) {
        if(!"by_lead_history".equals(beanMetaInfo.getTable()) || eventType != CanalEntry.EventType.INSERT) {
            return;
        }

        ObjectId operatorId = (ObjectId) doc.get("operator_id");
        List<Document> positions = MongoUtils.findManyBy("dim_staff_positions", ImmutableMap.of("staff_id", operatorId));

        // TODO: load duties in one query
        Set<Integer> roles = new HashSet<>();
        positions.forEach(position -> {
            ObjectId dutyObjectId = position.getObjectId("duty_id");
            Document duty = MongoUtils.findOneBy("dim_duty", ImmutableMap.of("_id", dutyObjectId));
            if (duty != null) {
                roles.add(duty.getInteger(ROW_ID));
            }
        });
        doc.append("roles", roles);
    }
}

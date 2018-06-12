package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.config.ClassMetaConfiguration;
import com.shbaoyuantech.config.FieldConvertor;
import com.shbaoyuantech.config.MongoUtils;
import com.shbaoyuantech.commons.CompanyConstant;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public class DataSynchronize {

    private static Map<String, String> tableMappingConfiguration;

    private static Map<String, Boolean> isCommonDataConfiguation;

    private static Map<String, Map<String, Map<String, Object>>> fieldConfiguration;

    private static Map<Class, Method> parseMethods;

    static {
        ClassMetaConfiguration instance = ClassMetaConfiguration.getInstance();
        tableMappingConfiguration = instance.getTableMappingConfiguration();
        isCommonDataConfiguation = instance.getIsCommonDataConfiguration();
        fieldConfiguration = instance.getFieldConfiguration();
        parseMethods = FieldConvertor.getParseMethodMap();
    }

    static void synchronizeData(RowChange rowChange, Entry entry, int company) {
        String currentTableName = entry.getHeader().getTableName();
        String mappingCollectionName = tableMappingConfiguration.get(currentTableName);
        if (StringUtils.isEmpty(mappingCollectionName)) {
            return;
        }

        //组装更新或新增字段数据
        for (RowData rowData : rowChange.getRowDatasList()) {
            Map<String, Object> data = prepareDataSynchronize(rowData.getAfterColumnsList(), currentTableName, company);

            //冗余字段，特殊处理
            if(isTableHavaRedundantField(currentTableName)){
                Map<String, Object> redundant = handleRedundantField(rowChange.getEventType(), currentTableName, rowData.getAfterColumnsList());

                if(!CollectionUtils.isEmpty(redundant)){
                    data.putAll(redundant);
                }
            }

            if (rowChange.getEventType().equals(EventType.INSERT)) {
                insertOne(data, currentTableName, company);
            }

            if (rowChange.getEventType().equals(EventType.UPDATE)) {
                int rowId = (int) data.remove("rowId");
                updateOne(data, currentTableName, rowId, company);
            }
        }
    }

    private static Map<String, Object> prepareDataSynchronize(List<Column> columns, String currentTableName, int company) {
        Map<String, Object> result = new HashMap<>();

        try {
            for (Column column : columns) {
                boolean isNeedSync = column.getUpdated() && isColumnNeedSync(currentTableName, column.getName());
                boolean isId = column.getName().equals("id");

                Map<String, Object> currentColumnConfig = fieldConfiguration.get(currentTableName).get(column.getName());

                if (isNeedSync || isId) {
                    Object mongoValue = null;
                    Method method = parseMethods.get(currentColumnConfig.get("type"));
                    if (currentColumnConfig.get("type").equals(ObjectId.class)) {
                        Boolean isCommonData = isCommonDataConfiguation.get(currentTableName);

                        if (isCommonData) {
                            mongoValue = method.invoke(FieldConvertor.class, column.getValue(), currentColumnConfig.get("refCollection"), CompanyConstant.COMMON_FLAG);
                        }
                        if (!isCommonData) {
                            mongoValue = method.invoke(FieldConvertor.class, column.getValue(), currentColumnConfig.get("refCollection"), company);
                        }

                    }else if(currentColumnConfig.get("type").equals(Date.class)){
                        mongoValue = method.invoke(FieldConvertor.class, column.getValue(), currentColumnConfig.get("field"), result);
                    } else {
                        mongoValue = method.invoke(FieldConvertor.class, column.getValue());
                    }
                    result.put((String) currentColumnConfig.get("field"), mongoValue);
                }
            }
        } catch (Exception e) {
            throw new BizException("error##DataSynchronize:: data synchronize has an error...", e);
        }
        return result;
    }


    private static void insertOne(Map<String, Object> data, String currentTableName, int company) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        if (!isCommonDataConfiguation.get(currentTableName)) {
            data.put("companyId", company);
        }
        boolean isExist = MongoUtils.checkIdempotent(data, tableMappingConfiguration.get(currentTableName), company);
        if(isExist){ return; }
        MongoUtils.insertOne(data, tableMappingConfiguration.get(currentTableName));
    }

    private static void updateOne(Map<String, Object> data, String currentTableName, int rowId, int company) {
        MongoUtils.updateOne(data, tableMappingConfiguration.get(currentTableName), rowId, company);
    }

    private static boolean isColumnNeedSync(String tableName, String columnName){
        return !CollectionUtils.isEmpty(fieldConfiguration.get(tableName).get(columnName));
    }

    private static boolean isTableHavaRedundantField(String tableName){
        return tableName.equals("by_lead_history") || tableName.equals("by_contract_submenu");
    }

    private static Map<String, Object> handleRedundantField(EventType eventType, String tableName, List<Column> columns){
        Map<String, Object> result = new HashMap<>();

        if(eventType.equals(EventType.INSERT) && tableName.equals("by_lead_history")){
            Column operatorId = getColumn(columns, "operator_id");
            ObjectId operatorObjectId = MongoUtils.accessCurrentObjectId(FieldConvertor.accessInt(operatorId.getValue()), "dim_staff", CompanyConstant.COMMON_FLAG);
            List<Document> documents = MongoUtils.accessDocumentsByObjectId("staff_id", operatorObjectId, "dim_staff_positions", CompanyConstant.COMMON_FLAG);

            List<Integer> roles = new ArrayList<>();

            if(CollectionUtils.isEmpty(documents)){
                result.put("roles", new ArrayList<>());
                return result;
            }

            for(Document document : documents){
                ObjectId dutyObjectId = document.getObjectId("duty_id");
                Object role = MongoUtils.accessFieldByCurrentObjectId("rowId", "dim_duty", dutyObjectId);
                roles.add(Integer.parseInt(String.valueOf(role)));
            }
            result.put("roles", roles);
        }

        if(tableName.equals("by_contract_submenu")){
            Column ccId = getColumn(columns, "cc_id");
            result.put("isStoreAllocated", ccId.getValue() == null);
        }

        return result;
    }

    private static Column getColumn(List<Column> columns, String columnName){
        for(Column column : columns){
            if(column.getName().equals(columnName)){
                return column;
            }
        }
        return null;
    }

}

package com.shbaoyuantech.sync;

import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.config.ClassMetaConfiguration;
import com.shbaoyuantech.config.FieldConvertor;
import com.shbaoyuantech.config.MongoUtils;
import com.shbaoyuantech.config.RedundantFieldConvertor;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSynchronize {

    private static Map<String, String> tableMappingConfiguration;

    private static Map<String, Boolean> isCommonDataConfiguation;

    private static Map<String, Map<String, Map<String, Object>>> fieldConfiguration;

    private static Map<String, Map<String, Map<String, Object>>> redundantFieldConfiguration;

    static {
        ClassMetaConfiguration instance = ClassMetaConfiguration.getInstance();
        tableMappingConfiguration = instance.getTableMappingConfiguration();
        isCommonDataConfiguation = instance.getIsCommonDataConfiguration();
        fieldConfiguration = instance.getFieldConfiguration();
        redundantFieldConfiguration = instance.getRedundantFieldConfiguration();
    }

    static void synchronizeData(final RowChange rowChange, Entry entry, final int companyId) {
        final String currentTableName = entry.getHeader().getTableName();
        String mappingCollectionName = tableMappingConfiguration.get(currentTableName);
        if (StringUtils.isEmpty(mappingCollectionName)) {
            return;
        }

        //组装更新或新增字段数据
        for (final RowData rowData : rowChange.getRowDatasList()) {
            Map<String, Object> data = prepareDataSynchronize(rowData.getAfterColumnsList(), currentTableName, companyId);

            if(isTableHavaRedundantField(currentTableName)){
                Map<String, Object> params = new HashMap<String, Object>(){{
                   put("tableName", currentTableName);
                   put("companyId", companyId);
                   put("columns", rowData.getAfterColumnsList());
                   //put("fieldConfigs", redundantFieldConfiguration.get(currentTableName));
                   put("eventType", rowChange.getEventType());

                }};
                RedundantFieldConvertor.parse(data, params);
            }

            if (rowChange.getEventType().equals(EventType.INSERT)) {
                insertOne(currentTableName, companyId, data);
            }

            if (rowChange.getEventType().equals(EventType.UPDATE)) {
                int rowId = (int) data.remove("rowId");
                updateOne(currentTableName, rowId, companyId, data);
            }
        }
    }

    private static Map<String, Object> prepareDataSynchronize(List<Column> columns, String currentTableName, final int companyId) {
        Map<String, Object> result = new HashMap<>();

        try {
            for (final Column column : columns) {
                boolean isNeedSync = column.getUpdated() && isColumnNeedSync(currentTableName, column.getName());
                boolean isId = column.getName().equals("id");

                final Map<String, Object> currentColumnConfig = fieldConfiguration.get(currentTableName).get(column.getName());

                if (isNeedSync || isId) {
                    final Map<String, Object> params = new HashMap<String, Object>(){{
                        put("type", currentColumnConfig.get("type"));
                        put("value", column.getValue());
                        put("fieldName", currentColumnConfig.get("field"));
                        put("companyId", companyId);
                        put("collName", currentColumnConfig.get("refCollection"));
                    }};

                    FieldConvertor.parse(result, params);
                }
            }
        } catch (Exception e) {
            throw new BizException("error##DataSynchronize:: data synchronize has an error...", e);
        }
        return result;
    }


    private static void insertOne(String currentTableName, int companyId, Map<String, Object> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        if (!isCommonDataConfiguation.get(currentTableName)) {
            data.put("companyId", companyId);
        }
        boolean isNull = MongoUtils.checkIdempotency(tableMappingConfiguration.get(currentTableName), companyId, data);
        if(!isNull){ return; }
        MongoUtils.insertOne(tableMappingConfiguration.get(currentTableName), data);
    }

    private static void updateOne(String currentTableName, final int rowId, final int companyId, Map<String, Object> data) {
        MongoUtils.updateOne(tableMappingConfiguration.get(currentTableName), new HashMap<String, Object>(){{
            put("rowId", rowId);
            put("compnyId", companyId);
        }}, data);
    }

    private static boolean isColumnNeedSync(String tableName, String columnName){
        return !CollectionUtils.isEmpty(fieldConfiguration.get(tableName).get(columnName));
    }

    private static boolean isTableHavaRedundantField(String tableName){
        return !CollectionUtils.isEmpty(redundantFieldConfiguration.get(tableName));
    }

}

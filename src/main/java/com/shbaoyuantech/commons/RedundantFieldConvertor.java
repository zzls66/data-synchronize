package com.shbaoyuantech.commons;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedundantFieldConvertor {

    public static void parse(Map<String, Object> result, Map<String, Object> params){
        String tableName = (String) params.get("tableName");
        List<Column> columns = (List<Column>) params.get("columns");
        //Map<String, Map<String, Object>> fieldConfigs = (Map<String, Map<String, Object>>) params.get("fieldConfigs");
        EventType enentType = (EventType) params.get("eventType");
        int companyId = (int) params.get("companyId");

        if(tableName.equals("by_contract_submenu")){
            Column ccId = getColumn(columns, "cc_id");
            result.put("isStoreAllocated", ccId.getValue() == null);
        }

        if(tableName.equals("by_lead_history") && enentType == EventType.INSERT){
            final Column operatorId = getColumn(columns, "operator_id");
            final ObjectId operatorObjectId = MongoUtils.findObjectId("dim_staff", CompanyType.COMMON,
                    new HashMap<String, Object>(){{ put("rowId", Integer.parseInt(operatorId.getValue())); }});
            List<Document> docs = MongoUtils.findManyBy("dim_staff_positions", CompanyType.COMMON, new HashMap<String, Object>() {{
                put("staff_id", operatorObjectId);
            }});

            final List<Integer> roles = new ArrayList<>();
            if(CollectionUtils.isEmpty(docs)){
                result.put("roles", roles);
                return;
            }

            for(Document doc : docs){
                final ObjectId dutyObjectId = doc.getObjectId("duty_id");
                int id = (int)MongoUtils.findFieldValueBy("dim_duty", "rowId", new HashMap<String, Object>() {{
                    put("companyId", CompanyType.COMMON);
                    int id = (int) put("_id", dutyObjectId);
                    roles.add(id);
                }});
                roles.add(id);
            }
            result.put("roles", roles);
        }
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

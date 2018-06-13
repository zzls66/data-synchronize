package com.shbaoyuantech.commons;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FieldConvertor {

    public static void parse(Map<String, Object> result, Map<String, Object> params){
         String value = (String) params.get("value");
         if(StringUtils.isEmpty(value)){
             return;
         }

         String fieldName = (String) params.get("fieldName");
         Class type = (Class) params.get("type");

         if(type.equals(String.class)){
            result.put(fieldName, value);
         }

         if(type.equals(Integer.class) || type.equals(int.class)){
            result.put(fieldName, Integer.parseInt(value));
         }

         if(type.equals(Boolean.class) || type.equals(boolean.class)){
             result.put(fieldName, Integer.parseInt(value) > 0);
         }

         if(type.equals(Double.class) || type.equals(double.class)){
             result.put(fieldName, parseDouble(value));
         }

         if(type.equals(Date.class)){
            parseDate(result, value, fieldName);
         }

         if(type.equals(ObjectId.class)){
             int companyId = (int) params.get("companyId");
             String collName = (String) params.get("collName");
             result.put(fieldName, parseObjectId(collName, companyId, value));
         }
    }

    private static Double parseDouble(String value){
        return new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static ObjectId parseObjectId(String collName, int companyId, final String value){
        return MongoUtils.findObjectId(collName, companyId, new HashMap<String, Object>(){{ put("rowId", Integer.parseInt(value)); }});
    }

    private static void parseDate(Map<String, Object> result, String value, String fieldName){
        ObjectId objectId = MongoUtils.findDateObjectId(value);
        result.put(fieldName, DateUtils.transStr2Date(value));

        result.put(fieldName.replace("Time", "Date") + "_id", objectId);
    }
}

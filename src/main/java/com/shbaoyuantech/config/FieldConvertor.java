package com.shbaoyuantech.config;

import com.shbaoyuantech.commons.BizException;
import com.shbaoyuantech.commons.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FieldConvertor {

//    public parse(Class type, Object value) {
//        if (value == null) {
//            return;
//        }
////        switch (type.toString()) {
////            case Integer.cl:
////                System.out.println("xx");
////                break;
////        }
//
//        if (type == String.class) {
//            return xxx;
//        }
//        if (type == String.class) {
//            return xxx;
//        }if (type == String.class) {
//            return xxx;
//        }if (type == String.class) {
//            return xxx;
//        }
//
//    }

    private static Map<Class, Method> parseMethodMap = new HashMap<>();

    public synchronized static Map<Class, Method> getParseMethodMap(){
        if(!CollectionUtils.isEmpty(parseMethodMap)){
            return parseMethodMap;
        }

        try{
            Method[] methods = FieldConvertor.class.getDeclaredMethods();
            for(Method method : methods){
                parseMethodMap.put(method.getReturnType(), method);
            }
            parseMethodMap.put(int.class, parseMethodMap.get(Integer.class));
            parseMethodMap.put(double.class, parseMethodMap.get(Double.class));
            parseMethodMap.put(boolean.class, parseMethodMap.get(Boolean.class));
        }catch (Exception e){
            throw new BizException("error##FieldConvertor::init field conventor fail...", e);
        }
        return parseMethodMap;
    }

    public static String accessString(String value){
        return value;
    }

    public static Integer accessInt(String value){
        return StringUtils.isEmpty(value) ? null : Integer.parseInt(value);
    }

    public static Boolean accessBoolean(String value){
        return StringUtils.isEmpty(value) ? null : Integer.parseInt(value) > 0;
    }

    public static Double accessDouble(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static ObjectId accessObjectId(String value, String collectionName, int company){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return MongoUtils.accessCurrentObjectId(Integer.parseInt(value), collectionName, company);
    }

    public static Date accessDate(String value, String fieldName, Map<String, Object> result){
        if(StringUtils.isEmpty(value)){
            return null;
        }

        ObjectId objectId = MongoUtils.accessDateObjectId(value);
        String dateObjectIdName = fieldName.replace("Time", "Date");
        result.put(dateObjectIdName + "_id", objectId);
        return DateUtils.transStr2Date(value);
    }
}

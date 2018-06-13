package com.shbaoyuantech.trial;

import com.shbaoyuantech.commons.FieldConvertor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestConfig {

    public static void main(String[] args) {
//        Map<String, Map<String, Object>> metaConfiguration = ClassMetaConfiguration.getMetaConfiguration();
//        System.out.println(JSONObject.toJSONString(metaConfiguration));

        try {
//            Method accessDouble = FieldConvertor.class.getMethod("accessDouble", String.class);
//            accessDouble.getReturnType();
//            Object invoke = accessDouble.invoke(FieldConvertor.class, "2");
//            System.out.println(invoke);
            Map<Class, Method> parseMethodMap = new HashMap<>();
            Method[] methods = FieldConvertor.class.getMethods();
            for(Method method : methods){
                parseMethodMap.put(method.getReturnType(), method);
            }

            parseMethodMap.put(int.class, parseMethodMap.get(Integer.class));

//            Method method = parseMethodMap.get(Double.class);
//            Object invoke = method.invoke(FieldConvertor.class, "234");
            Method method = parseMethodMap.get(int.class);
            System.out.println(method.getName());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

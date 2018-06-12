package com.shbaoyuantech.config;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import com.shbaoyuantech.commons.BizException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;

public class ClassMetaConfiguration {
    private static ClassMetaConfiguration instance;

    private Map<String, String> tableMappingConfiguration = new HashMap<>();

    private Map<String, Boolean> isCommonDataConfiguration = new HashMap<>();

    private Map<String, Map<String, Map<String, Object>>> fieldConfiguration = new HashMap<>();

    public synchronized static ClassMetaConfiguration getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new ClassMetaConfiguration();

        try {
            Reflections reflections = new Reflections();
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(MappingBean.class);

            for (Class clazz : classes) {
                MappingBean classAnno = (MappingBean) clazz.getAnnotation(MappingBean.class);
                instance.tableMappingConfiguration.put(classAnno.table(), classAnno.collection());
                instance.isCommonDataConfiguration.put(classAnno.table(), classAnno.isCommonData());

                Field[] fields = null;
                        //getAllFields(clazz);
                Map<String, Map<String, Object>> fieldConfigs = new HashMap<>();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MappingColumn.class)) {
                        Map<String, Object> currentField = new HashMap<>();
                        field.setAccessible(true);
                        MappingColumn fieldAnno = field.getAnnotation(MappingColumn.class);

                        currentField.put("field", fieldAnno.field());
                        currentField.put("refCollection", fieldAnno.refCollection());
                        currentField.put("type", field.getType());

                        fieldConfigs.put(fieldAnno.column(), currentField);
                    }
                }
                instance.fieldConfiguration.put(classAnno.table(), fieldConfigs);
            }
        } catch (Exception e) {
            throw new BizException("error##ClassMetaConfiguration::init class meta information fail...", e);
        }
        return instance;
    }

//    private static Field[] getAllFields(Class targetClass){
//        ArrayList<Field> fieldList = new ArrayList<>();
//        do {
//            fieldList.addAll(Arrays.asList(targetClass.getDeclaredFields()));
//            targetClass = targetClass.getSuperclass();
//        } while(targetClass != null && targetClass != Object.class);
//
////        return fieldList.toArray(new Field[32]);
//    }

    public Map<String, String> getTableMappingConfiguration() {
        return tableMappingConfiguration;
    }

    public Map<String, Boolean> getIsCommonDataConfiguration() {
        return isCommonDataConfiguration;
    }

    public Map<String, Map<String, Map<String, Object>>> getFieldConfiguration() {
        return fieldConfiguration;
    }

}

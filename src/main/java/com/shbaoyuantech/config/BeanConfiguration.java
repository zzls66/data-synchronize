package com.shbaoyuantech.config;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import com.shbaoyuantech.beans.BaseBean;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;

public class BeanConfiguration {
    private static final Map<Object, BeanMetaInfo> metaInfoMap = new HashMap<>();
    
    static {
        Reflections reflections = new Reflections();
        Set<Class<?>> mappingBeans = reflections.getTypesAnnotatedWith(MappingBean.class);

        mappingBeans.forEach(bean -> {
            MappingBean beanMapping = bean.getAnnotation(MappingBean.class);
            BeanMetaInfo beanMetaInfo = new BeanMetaInfo();
            beanMetaInfo.setTable(beanMapping.table());
            beanMetaInfo.setCollection(beanMapping.collection());

            List<Field> fields = getAllMappingFields(bean);
            fields.forEach(field ->  {
                MappingColumn fieldAnno = field.getAnnotation(MappingColumn.class);
                BeanMetaInfo.BeanField beanField = new BeanMetaInfo.BeanField();
                beanField.setColumn(fieldAnno.column());
                beanField.setField(fieldAnno.field());
                beanField.setFieldType(field.getType());
                Class<? extends BaseBean> refBean = fieldAnno.refBean();
                if (!refBean.equals(BaseBean.class)) {
                    beanField.setRefBean(refBean);
                }
                beanField.setRefDateField(fieldAnno.refDateField());
                beanField.setSupplemental(fieldAnno.isSupplemental());

                beanMetaInfo.addField(beanField);
            });
            metaInfoMap.put(beanMetaInfo.getTable(), beanMetaInfo);
            metaInfoMap.put(bean, beanMetaInfo);
        });

        resolveRefCollectionFromRefBean();
    }

    private static void resolveRefCollectionFromRefBean() {
        new HashSet<>(metaInfoMap.values()).forEach(beanMetaInfo ->
            beanMetaInfo.getFields().forEach(beanField -> {
                Class refBean = beanField.getRefBean();
                if (refBean != null) {
                    beanField.setRefCollection(metaInfoMap.get(refBean).getCollection());
                }
            })
        );
    }

    private static List<Field> getAllMappingFields(Class targetClass){
        List<Field> mappingFields = new ArrayList<>();
        do {
            List<Field> fields = Arrays.asList(targetClass.getDeclaredFields());
            fields.forEach(field -> {
                if (field.isAnnotationPresent(MappingColumn.class)) {
                    mappingFields.add(field);
                }
            });
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        return mappingFields;
    }

    public static BeanMetaInfo getBeanMetaInfo(String table) {
        return metaInfoMap.get(table);
    }
}

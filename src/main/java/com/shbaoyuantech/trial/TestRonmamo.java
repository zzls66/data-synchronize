package com.shbaoyuantech.trial;

import com.shbaoyuantech.annotations.MappingBean;
import com.shbaoyuantech.annotations.MappingColumn;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

public class TestRonmamo {

    public static void main(String[] args) {
        //一个方法，初始化一个 final static 的Map，作为全局的元数据
        //init 一个全局final static 的类，全是转化的方法，用field 的type类型getMethod 然后invoke
        Reflections reflections = new Reflections();
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(MappingBean.class);
        for (Class clazz : classes){
            System.out.println(clazz.toString());
            clazz.getMethods();
//            try {
//                clazz.getMethod("getMongoValue", Integer.class,int.class);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
            MappingBean mappingBean = (MappingBean)clazz.getAnnotation(MappingBean.class);
            mappingBean.table();
            Method[] declaredMethods = mappingBean.annotationType().getDeclaredMethods();
            for(Method method : declaredMethods){
                method.getName();
                method.getDeclaredAnnotations();
            }
            //System.out.println("================" + a.annotationType().getAnnotation(MappingBean.class).collectionName());
            //test(clazz.getAnnotation(MappingBean.class));
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                System.out.println(field.getName() + field.getAnnotation(MappingColumn.class));
                field.isAnnotationPresent(MappingColumn.class);
                MappingColumn annotation = field.getAnnotation(MappingColumn.class);
                String s = annotation.refCollection();
                Type genericType = field.getGenericType();
                System.out.println("type" + genericType.toString());
                //isDiffCompany, collectionName 作为另一个方法的入参代入
                //Map<tableName, Map<columnName, field/ columnAnnotation（转成map？？貌似没必要）>>

                // return insert or update document &is sub database
            }
        }
    }

//    public static void test(Annotation anno){
//        if(anno != null){
//            Method[] met = anno.annotationType().getDeclaredMethods();
//            for(Method me : met ){
//                if(!me.isAccessible()){
//                    me.setAccessible(true);
//                }
//                try {
//                    System.out.println("class anno value" + me.invoke(anno, null));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}

package com.shbaoyuantech.config;

import com.shbaoyuantech.beans.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BeanMetaInfo {
    private String table;
    private String collection;

    private List<BeanField> fields = new ArrayList<>();

    public BeanField getBeanField(String column) {
        for (BeanField field : fields) {
            if (field.getColumn().equals(column)) {
                return field;
            }
        }
        return null;
    }

    public boolean isColumnExistingInFields(String column) {
        for (BeanField field : fields) {
            if (column.equals(field.getColumn())) {
                return true;
            }
        }
        return false;
    }

    public List<BeanField> addField(BeanField field) {
        fields.add(field);
        return fields;
    }

    public List<BeanField> getFields() {
        return fields;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public static class BeanField {
        private String column;
        private String field;
        private Class fieldType;
        private Class<? extends BaseBean> refBean;
        private String refCollection;
        private String refDateField;
        private boolean supplemental;

        public String getRefCollection() {
            return refCollection;
        }

        public void setRefCollection(String refCollection) {
            this.refCollection = refCollection;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Class getFieldType() {
            return fieldType;
        }

        public void setFieldType(Class fieldType) {
            this.fieldType = fieldType;
        }

        public Class<? extends BaseBean> getRefBean() {
            return refBean;
        }

        public void setRefBean(Class<? extends BaseBean> refBean) {
            this.refBean = refBean;
        }

        public boolean isSupplemental() {
            return supplemental;
        }

        public void setSupplemental(boolean supplemental) {
            this.supplemental = supplemental;
        }

        public String getRefDateField() {
            return refDateField;
        }

        public void setRefDateField(String refDateField) {
            this.refDateField = refDateField;
        }
    }

}

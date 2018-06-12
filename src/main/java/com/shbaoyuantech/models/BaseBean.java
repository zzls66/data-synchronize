package com.shbaoyuantech.models;

import com.shbaoyuantech.annotations.MappingColumn;

public abstract class BaseBean {
    @MappingColumn(column = "id", field = "rowId")
    protected int rowId;

    @MappingColumn(column = "is_deleted", field = "isDeleted")
    protected boolean isDeleted;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

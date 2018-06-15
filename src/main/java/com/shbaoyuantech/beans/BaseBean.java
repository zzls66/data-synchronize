package com.shbaoyuantech.beans;

import com.shbaoyuantech.annotations.MappingColumn;

public abstract class BaseBean {
    @MappingColumn(column = "id", field = "rowId")
    protected int rowId;

    @MappingColumn(field = "dbCode", isSupplemental = true)
    protected int dbCode;

    @MappingColumn(column = "is_deleted", field = "isDeleted")
    protected boolean isDeleted;
}

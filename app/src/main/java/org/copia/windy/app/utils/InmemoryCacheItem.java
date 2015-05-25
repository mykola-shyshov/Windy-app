package org.copia.windy.app.utils;

import java.util.Date;

public class InmemoryCacheItem {

    private Object value;

    private Date updatedDate;

    public InmemoryCacheItem( Object value ) {
        this.value = value;
        this.updatedDate = new Date();
    }

    public Object getValue() {
        return value;
    }

    public void setValue( Object value ) {
        this.value = value;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate( Date updatedDate ) {
        this.updatedDate = updatedDate;
    }
}

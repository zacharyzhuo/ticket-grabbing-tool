package com.zachary.ticketgrabbingtool.json;

import com.zachary.ticketgrabbingtool.utils.ObjectUtil;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ObjectUtil.objectToJsonStr(this);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

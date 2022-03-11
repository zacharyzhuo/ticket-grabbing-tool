package com.zachary.ticketgrabbingtool.rent591.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class PostRequestModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int is_format_data;
    private int is_new_list;
    private int type;
    private int region;

    public int getIs_format_data() {
        return is_format_data;
    }

    public void setIs_format_data(int is_format_data) {
        this.is_format_data = is_format_data;
    }

    public int getIs_new_list() {
        return is_new_list;
    }

    public void setIs_new_list(int is_new_list) {
        this.is_new_list = is_new_list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

}

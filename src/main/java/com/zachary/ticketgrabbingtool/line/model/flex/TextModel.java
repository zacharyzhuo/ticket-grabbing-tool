package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextModel extends AbstractEntity implements IBoxContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "text";

    public static final List<String> TITLE_KEY = new ArrayList<>(Arrays.asList("price", "address", "section", "kind", "floor", "area"));
    public static final List<String> TITLE_VALUE = new ArrayList<>(Arrays.asList("租金", "地址", "地區", "類型", "樓層", "坪數"));

    public static final List<String> WEIGHT = new ArrayList<>(Arrays.asList("bold"));
    public static final List<String> SIZE = new ArrayList<>(Arrays.asList("md", "sm"));
    public static final List<String> COLOR = new ArrayList<>(Arrays.asList("#aaaaaa", "#666666"));
    public static final List<String> ALIGN = new ArrayList<>(Arrays.asList("center"));

    private String type;
    private String text;
    private String weight;
    private String size;
    private String color;
    private Integer flex;
    private Boolean wrap;
    private String align;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getFlex() {
        return flex;
    }

    public void setFlex(Integer flex) {
        this.flex = flex;
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}

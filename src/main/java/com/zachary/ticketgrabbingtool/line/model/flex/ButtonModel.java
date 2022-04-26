package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ButtonModel extends AbstractEntity implements IBoxContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "button";
    public static final List<String> STYLE = new ArrayList<>(Arrays.asList("primary"));
    public static final List<String> HEIGHT = new ArrayList<>(Arrays.asList("sm"));
    public static final List<String> ADJUSTMODE = new ArrayList<>(Arrays.asList("shrink-to-fit"));

    private String type;
    private ActionModel action;
    private String style;
    private String height;
    private String adjustMode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
        this.action = action;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAdjustMode() {
        return adjustMode;
    }

    public void setAdjustMode(String adjustMode) {
        this.adjustMode = adjustMode;
    }

}

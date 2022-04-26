package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoxModel extends AbstractEntity implements IBoxContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "box";
    public static final List<String> SPACING = new ArrayList<>(Arrays.asList("sm"));
    public static final List<String> MARGIN = new ArrayList<>(Arrays.asList("lg"));
    public static final List<String> LAYOUT = new ArrayList<>(Arrays.asList("vertical", "horizontal", "baseline"));

    private String type;
    private String layout;
    private String spacing;
    private String paddingAll;
    private String paddingStart;
    private String paddingTop;
    private String paddingBottom;
    private String margin;
    private List<IBoxContent> contents;
    private ActionModel action;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLayout() {
        return layout;
    }

    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public String getPaddingAll() {
        return paddingAll;
    }

    public void setPaddingAll(String paddingAll) {
        this.paddingAll = paddingAll;
    }

    public String getPaddingStart() {
        return paddingStart;
    }

    public void setPaddingStart(String paddingStart) {
        this.paddingStart = paddingStart;
    }

    public String getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(String paddingTop) {
        this.paddingTop = paddingTop;
    }

    public String getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(String paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<IBoxContent> getContents() {
        return contents;
    }

    public void setContents(List<IBoxContent> contents) {
        this.contents = contents;
    }

    public void addContents(IBoxContent content) {
        this.contents.add(content);
    }

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
        this.action = action;
    }

}

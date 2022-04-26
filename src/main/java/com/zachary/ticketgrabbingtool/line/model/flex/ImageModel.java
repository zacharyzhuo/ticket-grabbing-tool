package com.zachary.ticketgrabbingtool.line.model.flex;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageModel extends AbstractEntity implements IBoxContent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "image";
    public static final List<String> GRAVITY = new ArrayList<>(Arrays.asList("center"));
    public static final List<String> SIZE = new ArrayList<>(Arrays.asList("full"));
    public static final List<String> ASPECT_MODE = new ArrayList<>(Arrays.asList("cover"));

    private String type;
    private String url;
    private String gravity;
    private String size;
    private String aspectMode;
    private String aspectRatio;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGravity() {
        return gravity;
    }

    public void setGravity(String gravity) {
        this.gravity = gravity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAspectMode() {
        return aspectMode;
    }

    public void setAspectMode(String aspectMode) {
        this.aspectMode = aspectMode;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

}

package com.zachary.ticketgrabbingtool.rent591.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

import java.util.List;

public class PostModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int postId; // [文章編號] id
    private String title; // [文章標題] address_img_title
    private String kind; // [租屋類型] kind_name
    private String floor; // [樓層] floorInfo
    private String price; // [租金] price
    private String area; // [坪數] area
    private String section; // [行政區] section_name
    private String street; // [街區] street_name
    private String location; // [地址] location
    private List<String> photoList; // [照片] photoList

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

}

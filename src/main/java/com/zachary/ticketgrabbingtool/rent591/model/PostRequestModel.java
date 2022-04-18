package com.zachary.ticketgrabbingtool.rent591.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;

public class PostRequestModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // 必帶參數 ---------------
    private String is_format_data;
    private String is_new_list;
    private String type;
    private String region;
    private String searchtype;
    // -----------------------

    // 換頁 --------------------
    private String firstRow;
    private String totalRows;
    //  -----------------------

    // 篩選條件 ----------------
    // [單選-類型] 整層住家：1, 獨立套房：2, 分租套房：3, 雅房：4, 車位：8, 其他：24
    private String kind;
    // [多選-位置] 3：中山區, 5：大安區, 7：信義區, 8：士林區, 10：內湖區, 1：中正區, 6：萬華區, 4：松山區, 2：大同區, 12：文山區, 9：北投區, 11：南港區
    private String section;
    // [多選-租金] 0_5000, 5000_10000, 10000_20000, 20000_30000, 30000_40000, 40000_ (e.g., 0_5000,5000_10000)
    private String multiPrice;
    // [租金(自訂)] e.g., 13000,20000
    private String rentprice;
    // [多選-格局] 1房：1, 2房：2, 3房：3, 4房：4
    private String multiRoom;
    // [多選-特色] 新上架：newPost, 近捷運：near_subway, 可養寵物：pet, 可開伙：cook, 有車位：cartplace, 有電梯：lift, 有陽台：balcony_1, 可短期租賃：lease
    private String other;
    // [多選-型態] 公寓：1, 電梯大樓：2, 透天厝：3, 別墅：4
    private String shape;
    // [多選-坪數] 0_10, 10_20, 20-30, 30-40, 40-50, 50_
    private String multiArea;
    // [坪數(自訂)] e.g., 7,15
    private String area;
    // [多選-樓層] 0_1, 2_6, 6_12, 12_
    private String multiFloor;
    // [多選-設備] 冷氣：cold, 有洗衣機：washer, 有冰箱：icebox, 有熱水器：hotwater, 有天然瓦斯：naturalgas, 有網路：broadband, 床：bed
    private String option;
    // [多選-須知] 男女皆可：all_sex, 限男生：boy, 限女生：girl, 排除頂樓加蓋：not_cover
    private String multiNotice;
    // -----------------------

    // 排序 -------------------
    // [排序標準] 最新：posttime, 租金：money 坪數：area
    private String order;
    // [排序方式] desc, asc
    private String orderType;
    // ------------------------

    public String getIs_format_data() {
        return is_format_data;
    }

    public void setIs_format_data(String is_format_data) {
        this.is_format_data = is_format_data;
    }

    public String getIs_new_list() {
        return is_new_list;
    }

    public void setIs_new_list(String is_new_list) {
        this.is_new_list = is_new_list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSearchtype() {
        return searchtype;
    }

    public void setSearchtype(String searchtype) {
        this.searchtype = searchtype;
    }

    public String getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(String firstRow) {
        this.firstRow = firstRow;
    }

    public String getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(String totalRows) {
        this.totalRows = totalRows;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getMultiPrice() {
        return multiPrice;
    }

    public void setMultiPrice(String multiPrice) {
        this.multiPrice = multiPrice;
    }

    public String getRentprice() {
        return rentprice;
    }

    public void setRentprice(String rentprice) {
        this.rentprice = rentprice;
    }

    public String getMultiRoom() {
        return multiRoom;
    }

    public void setMultiRoom(String multiRoom) {
        this.multiRoom = multiRoom;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getMultiArea() {
        return multiArea;
    }

    public void setMultiArea(String multiArea) {
        this.multiArea = multiArea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMultiFloor() {
        return multiFloor;
    }

    public void setMultiFloor(String multiFloor) {
        this.multiFloor = multiFloor;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getMultiNotice() {
        return multiNotice;
    }

    public void setMultiNotice(String multiNotice) {
        this.multiNotice = multiNotice;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

}

package com.zachary.ticketgrabbingtool.httpclient.model;

import com.zachary.ticketgrabbingtool.json.AbstractEntity;
import org.apache.http.impl.client.BasicCookieStore;

import java.util.HashMap;
import java.util.List;

public class HeaderModel extends AbstractEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BasicCookieStore cookieStore;
    private List<HashMap<String, String>> headerList;

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public List<HashMap<String, String>> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<HashMap<String, String>> headerList) {
        this.headerList = headerList;
    }

}

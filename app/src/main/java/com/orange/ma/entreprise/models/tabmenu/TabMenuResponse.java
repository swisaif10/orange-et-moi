
package com.orange.ma.entreprise.models.tabmenu;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TabMenuResponse {

    @Expose
    private List<TabMenuItem> data;
    @Expose
    private String hash;

    public List<TabMenuItem> getData() {
        return data;
    }

    public void setData(List<TabMenuItem> data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}

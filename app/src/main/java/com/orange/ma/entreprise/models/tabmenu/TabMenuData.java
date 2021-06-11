package com.orange.ma.entreprise.models.tabmenu;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;


public class TabMenuData {

    @Expose
    private Header header;
    @Expose
    private TabMenuResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public TabMenuResponse getTabMenuResponse() {
        return response;
    }

    public void setTabMenuResponse(TabMenuResponse tabmenuResponse) {
        this.response = tabmenuResponse;
    }

}

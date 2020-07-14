
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class DashboardData {

    @Expose
    private Header header;
    @Expose
    private DashboardResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public DashboardResponse getResponse() {
        return response;
    }

    public void setResponse(DashboardResponse response) {
        this.response = response;
    }

}

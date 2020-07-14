
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

public class DashboardResponse {

    @Expose
    private DashboardResponseData data;

    public DashboardResponseData getData() {
        return data;
    }

    public void setData(DashboardResponseData data) {
        this.data = data;
    }

}

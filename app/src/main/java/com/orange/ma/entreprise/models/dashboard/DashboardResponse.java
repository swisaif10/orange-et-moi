
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

public class DashboardResponse {

    @Expose
    private DashboardResponseData data;
    @Expose
    private String hashTabMenu;
    @Expose
    private String hashTemplates;

    public DashboardResponseData getData() {
        return data;
    }

    public void setData(DashboardResponseData data) {
        this.data = data;
    }

    public String getHashTabMenu() {
        return hashTabMenu;
    }

    public void setHashTabMenu(String hashTabMenu) {
        this.hashTabMenu = hashTabMenu;
    }

    public String getHashTemplates() {
        return hashTemplates;
    }

    public void setHashTemplates(String hashTemplates) {
        this.hashTemplates = hashTemplates;
    }
}

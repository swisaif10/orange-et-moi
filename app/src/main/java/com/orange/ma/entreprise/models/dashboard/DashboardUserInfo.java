package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

public class DashboardUserInfo {

    @Expose
    private String stringFidelity;
    @Expose
    private String businessName;

    public String getStringFidelity() {
        return stringFidelity;
    }

    public void setStringFidelity(String stringFidelity) {
        this.stringFidelity = stringFidelity;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}

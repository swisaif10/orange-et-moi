
package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfos {

    @Expose
    private String businessName;
    @Expose
    private String stringFidelity;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getStringFidelity() {
        return stringFidelity;
    }

    public void setStringFidelity(String stringFidelity) {
        this.stringFidelity = stringFidelity;
    }
}

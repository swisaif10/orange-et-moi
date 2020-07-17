
package com.orange.ma.entreprise.models.guest;

import com.google.gson.annotations.Expose;

public class GuestLoginResponse {

    @Expose
    private GuestLoginResponseData data;
    @Expose
    private String hashTemplates;

    public GuestLoginResponseData getData() {
        return data;
    }

    public void setData(GuestLoginResponseData data) {
        this.data = data;
    }

    public String getHashTemplates() {
        return hashTemplates;
    }

    public void setHashTemplates(String hashTemplates) {
        this.hashTemplates = hashTemplates;
    }

}

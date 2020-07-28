package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;

public class SettingTagResponse {
    @Expose
    private SettingTagResponseData data;

    public SettingTagResponseData getData() {
        return data;
    }

    public void setData(SettingTagResponseData data) {
        this.data = data;
    }
}

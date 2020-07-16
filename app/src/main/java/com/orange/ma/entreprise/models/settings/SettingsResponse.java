
package com.orange.ma.entreprise.models.settings;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SettingsResponse {

    @Expose
    private List<SettingsItem> data;

    public List<SettingsItem> getData() {
        return data;
    }

    public void setData(List<SettingsItem> data) {
        this.data = data;
    }

}

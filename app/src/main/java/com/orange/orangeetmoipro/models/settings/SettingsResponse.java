
package com.orange.orangeetmoipro.models.settings;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SettingsResponse {

    @Expose
    private ArrayList<SettingsItem> data;

    public ArrayList<SettingsItem> getData() {
        return data;
    }

    public void setData(ArrayList<SettingsItem> data) {
        this.data = data;
    }

}

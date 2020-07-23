
package com.orange.ma.entreprise.models.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsItem {

    @Expose
    private String action;
    @Expose
    private String actionType;
    @Expose
    private String icon;
    @Expose
    private String status;
    @Expose
    private String title;
    @Expose
    private boolean inApp;
    @Expose
    private String urlVebView;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInApp() {
        return inApp;
    }

    public void setInApp(boolean inApp) {
        this.inApp = inApp;
    }

    public String getUrlVebView() {
        return urlVebView;
    }

    public void setUrlVebView(String urlVebView) {
        this.urlVebView = urlVebView;
    }
}

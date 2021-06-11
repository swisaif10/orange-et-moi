package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class SettingCompleteAccount implements Serializable {

    @Expose
    private String action;
    @Expose
    private String actionType;
    @Expose
    private String icon;
    @Expose
    private Boolean inApp;
    @Expose
    private String status;
    @Expose
    private String title;

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

    public Boolean getInApp() {
        return inApp;
    }

    public void setInApp(Boolean inApp) {
        this.inApp = inApp;
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

}

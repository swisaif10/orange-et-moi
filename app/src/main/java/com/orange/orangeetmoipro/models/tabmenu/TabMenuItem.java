
package com.orange.orangeetmoipro.models.tabmenu;

import com.google.gson.annotations.Expose;

public class TabMenuItem {

    @Expose
    private String action;
    @Expose
    private String actionType;
    @Expose
    private String iconDisabled;
    @Expose
    private String iconEnabled;
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

    public String getIconDisabled() {
        return iconDisabled;
    }

    public void setIconDisabled(String iconDisabled) {
        this.iconDisabled = iconDisabled;
    }

    public String getIconEnabled() {
        return iconEnabled;
    }

    public void setIconEnabled(String iconEnabled) {
        this.iconEnabled = iconEnabled;
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

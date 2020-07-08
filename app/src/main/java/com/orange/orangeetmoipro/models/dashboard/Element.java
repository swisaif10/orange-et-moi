
package com.orange.orangeetmoipro.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element {

//    @Expose
//    private String action;
//    @Expose
//    private String actionType;
    @Expose
    private String color;
//    @SerializedName("in_app")
//    private Boolean inApp;
    @Expose
    private String type;
    @Expose
    private String value;

//    public String getAction() {
//        return action;
//    }
//
//    public void setAction(String action) {
//        this.action = action;
//    }
//
//    public String getActionType() {
//        return actionType;
//    }
//
//    public void setActionType(String actionType) {
//        this.actionType = actionType;
//    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

//    public Boolean getInApp() {
//        return inApp;
//    }
//
//    public void setInApp(Boolean inApp) {
//        this.inApp = inApp;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}


package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

public class Element {

    @Expose
    private String color;
    @Expose
    private String type;
    @Expose
    private String value;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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

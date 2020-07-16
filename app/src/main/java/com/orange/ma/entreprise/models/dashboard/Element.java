
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Element {

    @Expose
    private String color;
    @Expose
    private String type;
    @Expose
    private String value;
    @Expose
    @SerializedName("hover_txt_color")
    private String hoverTxtColor;

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

    public String getHoverTxtColor() {
        return hoverTxtColor;
    }

    public void setHoverTxtColor(String hoverTxtColor) {
        this.hoverTxtColor = hoverTxtColor;
    }
}

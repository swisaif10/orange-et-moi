package com.orange.ma.entreprise.models.listmsisdn;

import com.google.gson.annotations.Expose;

public class StringListMsisdn {

    @Expose
    private String select_my_lines_description;

    @Expose
    private String select_my_lines_placeholder;

    @Expose
    private String select_my_lines_label_button;

    public String getSelect_my_lines_description() {
        return select_my_lines_description;
    }

    public void setSelect_my_lines_description(String select_my_lines_description) {
        this.select_my_lines_description = select_my_lines_description;
    }

    public String getSelect_my_lines_placeholder() {
        return select_my_lines_placeholder;
    }

    public void setSelect_my_lines_placeholder(String select_my_lines_placeholder) {
        this.select_my_lines_placeholder = select_my_lines_placeholder;
    }

    public String getSelect_my_lines_label_button() {
        return select_my_lines_label_button;
    }

    public void setSelect_my_lines_label_button(String select_my_lines_label_button) {
        this.select_my_lines_label_button = select_my_lines_label_button;
    }
}

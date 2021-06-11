package com.orange.ma.entreprise.models.cgu;

import com.google.gson.annotations.Expose;

public class CGU {

    @Expose
    private String description;
    @Expose
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

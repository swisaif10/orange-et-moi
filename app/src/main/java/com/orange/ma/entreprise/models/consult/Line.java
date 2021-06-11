package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

public class Line {

    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

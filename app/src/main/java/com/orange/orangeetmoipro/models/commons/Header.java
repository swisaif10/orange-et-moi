
package com.orange.orangeetmoipro.models.commons;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Header {

    @Expose
    private int code;
    @Expose
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

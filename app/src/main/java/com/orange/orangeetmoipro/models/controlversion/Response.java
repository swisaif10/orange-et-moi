
package com.orange.orangeetmoipro.models.controlversion;


import com.google.gson.annotations.Expose;


public class Response {

    @Expose
    private String link;
    @Expose
    private String message;
    @Expose
    private String status;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

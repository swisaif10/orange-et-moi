package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class LoginData {

    @Expose
    private Header header;
    @Expose
    private LoginResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public LoginResponse getResponse() {
        return response;
    }

    public void setResponse(LoginResponse response) {
        this.response = response;
    }

}

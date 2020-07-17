
package com.orange.ma.entreprise.models.guest;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class GuestLoginData {

    @Expose
    private Header header;
    @Expose
    private GuestLoginResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public GuestLoginResponse getResponse() {
        return response;
    }

    public void setResponse(GuestLoginResponse response) {
        this.response = response;
    }

}

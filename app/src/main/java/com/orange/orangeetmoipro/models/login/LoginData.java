
package com.orange.orangeetmoipro.models.login;

import com.google.gson.annotations.Expose;
import com.orange.orangeetmoipro.models.commons.Header;

@SuppressWarnings("unused")
public class LoginData {

    @Expose
    private Header header;
    @Expose
    private Object response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

}

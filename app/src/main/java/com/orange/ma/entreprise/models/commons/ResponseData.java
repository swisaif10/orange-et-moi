
package com.orange.ma.entreprise.models.commons;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

@SuppressWarnings("unused")
public class ResponseData {

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

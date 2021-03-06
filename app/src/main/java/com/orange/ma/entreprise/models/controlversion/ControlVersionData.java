
package com.orange.ma.entreprise.models.controlversion;


import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;


public class ControlVersionData {

    @Expose
    private Header header;
    @Expose
    private Response response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}

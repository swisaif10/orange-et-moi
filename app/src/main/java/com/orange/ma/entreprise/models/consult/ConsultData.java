package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class ConsultData {

    @Expose
    private Header header;
    @Expose
    private ConsultResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ConsultResponse getResponse() {
        return response;
    }

    public void setResponse(ConsultResponse response) {
        this.response = response;
    }
}

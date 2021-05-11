package com.orange.ma.entreprise.models.listmsisdn;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class ListMsisdnData {

    @Expose
    private Header header;
    @Expose
    private ListMsisdnResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ListMsisdnResponse getResponse() {
        return response;
    }

    public void setResponse(ListMsisdnResponse response) {
        this.response = response;
    }
}

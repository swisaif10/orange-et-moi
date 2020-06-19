
package com.orange.orangeetmoipro.models.cgu;

import com.google.gson.annotations.Expose;
import com.orange.orangeetmoipro.models.commons.Header;

public class CGUData {

    @Expose
    private Header header;
    @Expose
    private CGUResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public CGUResponse getCGUResponse() {
        return response;
    }

    public void setCGUResponse(CGUResponse CGUResponse) {
        this.response = CGUResponse;
    }

}

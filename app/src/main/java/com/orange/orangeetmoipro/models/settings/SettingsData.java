
package com.orange.orangeetmoipro.models.settings;

import com.google.gson.annotations.Expose;
import com.orange.orangeetmoipro.models.commons.Header;

public class SettingsData {

    @Expose
    private Header header;
    @Expose
    private SettingsResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public SettingsResponse getResponse() {
        return response;
    }

    public void setResponse(SettingsResponse response) {
        this.response = response;
    }

}

package com.orange.ma.entreprise.models.login;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.commons.Header;

public class SettingTagData {
    @Expose
    private Header header;
    @Expose
    private SettingTagResponse response;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public SettingTagResponse getResponse() {
        return response;
    }

    public void setResponse(SettingTagResponse response) {
        this.response = response;
    }
}

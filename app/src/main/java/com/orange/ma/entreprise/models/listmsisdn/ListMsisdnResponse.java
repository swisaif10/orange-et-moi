package com.orange.ma.entreprise.models.listmsisdn;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ListMsisdnResponse {
    @Expose
    private List<Msisdn> data;

    @Expose
    private StringListMsisdn strings;

    public List<Msisdn> getData() {
        return data;
    }

    public void setData(List<Msisdn> data) {
        this.data = data;
    }

    public StringListMsisdn getStrings() {
        return strings;
    }

    public void setStrings(StringListMsisdn strings) {
        this.strings = strings;
    }
}
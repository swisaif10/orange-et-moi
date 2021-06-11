package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

public class ConsultResponse {

    @Expose
    private DataConsult data;
    @Expose
    private StringConsult strings;

    public DataConsult getData() {
        return data;
    }

    public void setData(DataConsult data) {
        this.data = data;
    }

    public StringConsult getStrings() {
        return strings;
    }

    public void setStrings(StringConsult strings) {
        this.strings = strings;
    }
}

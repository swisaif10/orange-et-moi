package com.orange.ma.entreprise.models.cgu;

import com.google.gson.annotations.Expose;

public class CGUResponse {

    @Expose
    private CGU data;

    public CGU getCGU() {
        return data;
    }

    public void setCGU(CGU cgu) {
        this.data = cgu;
    }

}

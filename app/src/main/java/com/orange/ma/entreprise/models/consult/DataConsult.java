package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

public class DataConsult {

    @Expose
    private String status;
    @Expose
    private String code_puk;

    @Expose
    private boolean is_shareable_code_puk;

    @Expose
    private String profile_name;

    @Expose
    private Balance balance;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode_puk() {
        return code_puk;
    }

    public void setCode_puk(String code_puk) {
        this.code_puk = code_puk;
    }

    public boolean isIs_shareable_code_puk() {
        return is_shareable_code_puk;
    }

    public void setIs_shareable_code_puk(boolean is_shareable_code_puk) {
        this.is_shareable_code_puk = is_shareable_code_puk;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}

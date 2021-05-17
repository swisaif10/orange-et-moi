package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

public class StringConsult {

    @Expose
    private String num_line;
    @Expose
    private String label_status_line;
    @Expose
    private String label_code_puk;
    @Expose
    private String last_update_balance;

    public String getNum_line() {
        return num_line;
    }

    public void setNum_line(String num_line) {
        this.num_line = num_line;
    }

    public String getLabel_status_line() {
        return label_status_line;
    }

    public void setLabel_status_line(String label_status_line) {
        this.label_status_line = label_status_line;
    }

    public String getLabel_code_puk() {
        return label_code_puk;
    }

    public void setLabel_code_puk(String label_code_puk) {
        this.label_code_puk = label_code_puk;
    }

    public String getLast_update_balance() {
        return last_update_balance;
    }

    public void setLast_update_balance(String last_update_balance) {
        this.last_update_balance = last_update_balance;
    }
}

package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Balance {

    @Expose
    private List<Line> line_balance;

    public List<Line> getLine_balance() {
        return line_balance;
    }

    public void setLine_balance(List<Line> line_balance) {
        this.line_balance = line_balance;
    }
}

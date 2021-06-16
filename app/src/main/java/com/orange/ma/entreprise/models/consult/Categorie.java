package com.orange.ma.entreprise.models.consult;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Categorie {

    @Expose
    private String name;
    @Expose
    private List<Line> line_balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Line> getLine_balance() {
        return line_balance;
    }

    public void setLine_balance(List<Line> line_balance) {
        this.line_balance = line_balance;
    }
}

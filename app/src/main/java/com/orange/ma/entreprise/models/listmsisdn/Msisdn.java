package com.orange.ma.entreprise.models.listmsisdn;

import com.google.gson.annotations.Expose;

public class Msisdn {

    @Expose
    private String line;
    @Expose
    private String planCode;
    @Expose
    private String planLabel;
    @Expose
    private String type;
    @Expose
    private String status;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanLabel() {
        return planLabel;
    }

    public void setPlanLabel(String planLabel) {
        this.planLabel = planLabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class DashboardResponseData {

    @Expose
    private ArrayList<Template> templates;

    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        this.templates = templates;
    }

}

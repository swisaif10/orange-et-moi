
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;

import java.util.List;

public class DashboardResponseData {

    @Expose
    private List<Template> templates;

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

}

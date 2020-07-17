
package com.orange.ma.entreprise.models.guest;

import com.google.gson.annotations.Expose;
import com.orange.ma.entreprise.models.dashboard.Template;

import java.util.List;

public class GuestLoginResponseData {

    @Expose
    private List<Template> templates;

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

}

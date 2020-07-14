
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Template {

    public static final int TEMPLATE_BILLING = 0;
    public static final int TEMPLATE_PARCK = 1;
    public static final int TEMPLATE_LIST_SLIDER = 2;
    public static final int TEMPLATE_SMALL_LIST = 3;
    public static final int TEMPLATE_LIST = 4; //mode visiteur

    @SerializedName("color_icone")
    private String colorIcone;
    @SerializedName("element_complex")
    private ArrayList<ElementComplex> elementComplex;
    @Expose
    private String icon;
    @Expose
    private Long order;
    @Expose
    private String status;
    @Expose
    private String size;
    @SerializedName("template_key")
    private String templateKey;
    @Expose
    private String title;
    @SerializedName("type_template")
    private String typeTemplate;

    public String getColorIcone() {
        return colorIcone;
    }

    public void setColorIcone(String colorIcone) {
        this.colorIcone = colorIcone;
    }

    public ArrayList<ElementComplex> getElementComplex() {
        return elementComplex;
    }

    public void setElementComplex(ArrayList<ElementComplex> elementComplex) {
        this.elementComplex = elementComplex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(String typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

}

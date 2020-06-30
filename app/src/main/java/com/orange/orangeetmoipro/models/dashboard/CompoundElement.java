
package com.orange.orangeetmoipro.models.dashboard;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class CompoundElement {

    @Expose
    private ArrayList<Element> elements;
    @Expose
    private Long order;

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

}

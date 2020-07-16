
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ElementComplex {

    @SerializedName("element_compound")
    private List<CompoundElement> compoundElements;
    @SerializedName("type_element")
    private String typeElement;

    public List<CompoundElement> getCompoundElements() {
        return compoundElements;
    }

    public void setCompoundElements(List<CompoundElement> compoundElements) {
        this.compoundElements = compoundElements;
    }

    public String getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(String typeElement) {
        this.typeElement = typeElement;
    }

}

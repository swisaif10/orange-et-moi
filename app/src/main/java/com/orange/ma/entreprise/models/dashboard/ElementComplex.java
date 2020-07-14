
package com.orange.ma.entreprise.models.dashboard;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ElementComplex {

    @SerializedName("element_compound")
    private ArrayList<CompoundElement> compoundElements;
    @SerializedName("type_element")
    private String typeElement;

    public ArrayList<CompoundElement> getCompoundElements() {
        return compoundElements;
    }

    public void setCompoundElements(ArrayList<CompoundElement> compoundElements) {
        this.compoundElements = compoundElements;
    }

    public String getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(String typeElement) {
        this.typeElement = typeElement;
    }

}

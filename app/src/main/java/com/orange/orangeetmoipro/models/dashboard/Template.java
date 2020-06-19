package com.orange.orangeetmoipro.models.dashboard;

import java.util.ArrayList;

public class Template {

    private int icon1;
    private int icon2;
    private String title1;
    private String title2;
    private String color1;
    private String color2;
    private String type1;
    private String type2;
    private ArrayList<ItemButton> itemButtons1;
    private ArrayList<ItemButton> itemButtons2;
    private ArrayList<ItemString> itemStrings1;
    private ArrayList<ItemString> itemStrings2;

    public Template(int icon1, int icon2, String title1, String title2, String color1, String color2, String type1, String type2, ArrayList<ItemButton> itemButtons1, ArrayList<ItemButton> itemButtons2, ArrayList<ItemString> itemStrings1, ArrayList<ItemString> itemStrings2) {
        this.icon1 = icon1;
        this.icon2 = icon2;
        this.title1 = title1;
        this.title2 = title2;
        this.color1 = color1;
        this.color2 = color2;
        this.type1 = type1;
        this.type2 = type2;
        this.itemButtons1 = itemButtons1;
        this.itemButtons2 = itemButtons2;
        this.itemStrings1 = itemStrings1;
        this.itemStrings2 = itemStrings2;
    }

    public int getIcon1() {
        return icon1;
    }

    public void setIcon1(int icon1) {
        this.icon1 = icon1;
    }

    public int getIcon2() {
        return icon2;
    }

    public void setIcon2(int icon2) {
        this.icon2 = icon2;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public ArrayList<ItemButton> getItemButtons1() {
        return itemButtons1;
    }

    public void setItemButtons1(ArrayList<ItemButton> itemButtons1) {
        this.itemButtons1 = itemButtons1;
    }

    public ArrayList<ItemButton> getItemButtons2() {
        return itemButtons2;
    }

    public void setItemButtons2(ArrayList<ItemButton> itemButtons2) {
        this.itemButtons2 = itemButtons2;
    }

    public ArrayList<ItemString> getItemStrings1() {
        return itemStrings1;
    }

    public void setItemStrings1(ArrayList<ItemString> itemStrings1) {
        this.itemStrings1 = itemStrings1;
    }

    public ArrayList<ItemString> getItemStrings2() {
        return itemStrings2;
    }

    public void setItemStrings2(ArrayList<ItemString> itemStrings2) {
        this.itemStrings2 = itemStrings2;
    }
}

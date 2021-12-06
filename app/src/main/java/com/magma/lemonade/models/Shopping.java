package com.magma.lemonade.models;

import java.util.Locale;

public class Shopping {
    private String name;
    private int price;
    private String desc;

    public Shopping() {}

    public Shopping(String name, int price, String desc) {
        this.name = name;
        this.price = price;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEquipped() {
        return price != 0;
    }
}

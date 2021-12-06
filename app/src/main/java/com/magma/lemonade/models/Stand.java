package com.magma.lemonade.models;

import java.util.ArrayList;

public class Stand {
    private ArrayList<Product> products;
    private String startDate;

    public Stand() {}

    public Stand(ArrayList<Product> products, String startDate) {
        this.products = products;
        this.startDate = startDate;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}

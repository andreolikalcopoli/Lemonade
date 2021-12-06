package com.magma.lemonade.models;

public class Product {
    private String name;
    private boolean image;
    private double portionPrice;
    private double portionExpenses;
    private int soldItems;
    private double change;

    public Product() {}

    public Product(String name, double portionPrice, double portionExpenses, boolean image,  int soldItems, double change) {
        this.name = name;
        this.image = image;
        this.portionPrice = portionPrice;
        this.portionExpenses = portionExpenses;
        this.soldItems = soldItems;
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public double getPortionPrice() {
        return portionPrice;
    }

    public void setPortionPrice(double portionPrice) {
        this.portionPrice = portionPrice;
    }

    public double getPortionExpenses() {
        return portionExpenses;
    }

    public void setPortionExpenses(double portionExpenses) {
        this.portionExpenses = portionExpenses;
    }

    public int getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(int soldItems) {
        this.soldItems = soldItems;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void incrementSold() {
        this.soldItems += 1;
    }

    public void incrementChange(double change) {
        this.change += change;
    }
}

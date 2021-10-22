package com.magma.lemonade.models;

import java.util.ArrayList;

public class Stand {
    private String selling;
    private ArrayList<Product> products;
    private String notes;
    private String location;
    private double portionPrice;
    private double portionExpenses;
    private String startDate;
    private int soldItems;
    private double change;

    public Stand() {}

    public Stand(String selling, ArrayList<Product> products, String notes, String location, double portionPrice, double portionExpenses, String startDate, int soldItems, double change) {
        this.selling = selling;
        this.products = products;
        this.notes = notes;
        this.location = location;
        this.portionPrice = portionPrice;
        this.portionExpenses = portionExpenses;
        this.startDate = startDate;
        this.soldItems = soldItems;
        this.change = change;
    }

    public String getSelling() {
        return selling;
    }

    public void setSelling(String selling) {
        this.selling = selling;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(int soldItems) {
        this.soldItems = soldItems;
    }

    public void incrementSold() {
        this.soldItems += 1;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void incrementChange(double inc){
        this.change += inc;
    }
}

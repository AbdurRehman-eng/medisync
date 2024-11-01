package com.example.layouts;

public class Product {

    private String name;
    private String formula;
    private String price;

    public Product() {

    }

    public Product(String name, String formula, String price) {
        this.name = name;
        this.formula = formula;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

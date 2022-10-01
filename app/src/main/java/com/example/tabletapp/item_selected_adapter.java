package com.example.tabletapp;

import java.util.List;

public class item_selected_adapter {
    private String itemName;
    private String quantity;
    private String description;
    private String price, price22;
    private String itemId;
    private String category;
    private List<String> blah;

    public item_selected_adapter(String itemName, String description, String price, String itemId, String category, String price22) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.itemId = itemId;
        this.quantity = getQuantity();
        this.category = category;
        this.price22 =price22;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getItemId() {
        return itemId;
    }

    public String getCategory() {
        return category;
    }

    public item_selected_adapter(List<String> blah) {
        this.blah = blah;
    }

    public List<String> getBlah() {
        return blah;
    }

    public void setBlah(List<String> blah) {
        this.blah = blah;
    }

    public String getPrice22() {
        return price22;
    }
}

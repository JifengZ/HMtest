package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The ItemChild
 */
public class ItemChild {
    private final List<Item> listItem = new ArrayList<>();
    private String itemChildText;

    public String getItemChildText() {
        return itemChildText;
    }

    public void setItemChildText(String itemChildText) {
        this.itemChildText = itemChildText;
    }

    public List<Item> getListItem() {
        return listItem;
    }
}

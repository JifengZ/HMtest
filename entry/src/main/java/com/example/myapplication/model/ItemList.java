package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The ItemList
 */
public class ItemList {
    private final List<Item> listItem = new ArrayList<>();
    private final List<ItemChild> listItemChild = new ArrayList<>();

    public List<ItemChild> getListItemChild() {
        return listItemChild;
    }

    public List<Item> getListItem() {
        return listItem;
    }
}

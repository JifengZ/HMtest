package com.example.myapplication.data;

import com.example.myapplication.ResourceTable;
import com.example.myapplication.model.CategoryList;
import com.example.myapplication.model.Item;
import com.example.myapplication.model.ItemChild;
import com.example.myapplication.model.ItemList;

import ohos.app.Context;

import java.util.HashMap;

/**
 * The CategoryData, Generate the javaBean of categoryPage
 */
public class CategoryData {
    // The number of category on the left of page
    private static final int CATEGORY_COUNT = 7;

    // The number of item on the right-top of page
    private static final int ITEM_COUNT_PER_CATEGORY = 4;

    // The number of itemChild on the right-main of page
    private static final int ITEM_CHILD_COUNT_PER_CATEGORY = 3;

    // The number of item for the itemChild
    private static final int ITEM_COUNT_PER_CHILD = 6;

    private final CategoryList categoryList = new CategoryList();
    private final HashMap<String, ItemList> categoryItem = new HashMap<>();

    // Generate the javaBean of categoryPage
    public CategoryData(Context context) {
        String categoryName = context.getString(ResourceTable.String_category);
        String itemChildName = context.getString(ResourceTable.String_itemChild);
        String itemName = context.getString(ResourceTable.String_item);

        for (int i = 0; i < CATEGORY_COUNT; i++) {
            String categoryNameItem = categoryName + (i + 1);
            categoryList.getListText().add(categoryNameItem);

            ItemList itemList = new ItemList();
            for (int j = 0; j < ITEM_COUNT_PER_CATEGORY; j++) {
                Item item = new Item();
                item.setItemText((i + 1) + "_" + itemName + (j + 1));
                itemList.getListItem().add(item);
            }

            for (int j = 0; j < ITEM_CHILD_COUNT_PER_CATEGORY; j++) {
                ItemChild itemChild = new ItemChild();
                itemChild.setItemChildText((i + 1) + "_" + itemChildName + (j + 1));

                for (int k = 0; k < ITEM_COUNT_PER_CHILD; k++) {
                    Item item = new Item();
                    item.setItemText(itemName + (k + 1));
                    itemChild.getListItem().add(item);
                }
                itemList.getListItemChild().add(itemChild);
            }
            categoryItem.put(categoryNameItem, itemList);
        }
    }

    /**
     * The CategoryList, get categoryList(the javaBean)
     *
     * @return categoryList(the javaBean)
     */
    public CategoryList getCategoryList() {
        return categoryList;
    }

    /**
     * The getItemList, get bean of each category
     *
     * @param categoryName categoryName, name of category
     * @return ItemList(bean)
     */
    public ItemList getItemList(String categoryName) {
        return categoryItem.get(categoryName);
    }
}

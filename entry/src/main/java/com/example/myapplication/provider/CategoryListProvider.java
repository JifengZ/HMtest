package com.example.myapplication.provider;

import com.example.myapplication.ResourceTable;
import com.example.myapplication.utils.ElementUtil;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.agp.utils.TextTool;
import ohos.app.AbilityContext;

import java.util.List;
import java.util.Locale;

/**
 * The CategoryListProvider, the provider of ListContainer for category
 */
public class CategoryListProvider extends BaseItemProvider {
    private final List<String> itemList;
    private final AbilityContext context;

    // The index which select
    private int selectIndex = 0;

    /**
     * CategoryListProvider Constructor
     *
     * @param itemList itemList
     * @param context  context
     */
    public CategoryListProvider(List<String> itemList, AbilityContext context) {
        this.itemList = itemList;
        this.context = context;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public String getItem(int index) {
        return itemList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public Component getComponent(int index, Component component, ComponentContainer componentContainer) {
        Component itemComponent = component;
        CategoryListViewHolder viewHolder;
        if (itemComponent == null) {
            itemComponent =
                    LayoutScatter.getInstance(context)
                            .parse(ResourceTable.Layout_category_list_per, componentContainer, false);
        }

        viewHolder = new CategoryListViewHolder();
        viewHolder.text = (Text) itemComponent.findComponentById(ResourceTable.Id_categoryListPerText);
        viewHolder.text.setText(getItem(index));

        if (TextTool.isLayoutRightToLeft(Locale.getDefault())) {
            viewHolder.text.setTextAlignment(TextAlignment.VERTICAL_CENTER | TextAlignment.RIGHT);
        } else {
            viewHolder.text.setTextAlignment(TextAlignment.VERTICAL_CENTER | TextAlignment.LEFT);
        }

        if (selectIndex == index) {
            viewHolder.text.setTextColor(new Color(ElementUtil.getColor(context, ResourceTable.Color_primary_color)));
        } else {
            viewHolder.text.setTextColor(new Color(ElementUtil.getColor(context, ResourceTable.Color_primary_default)));
        }

        return itemComponent;
    }

    private static class CategoryListViewHolder {
        Text text;
    }
}

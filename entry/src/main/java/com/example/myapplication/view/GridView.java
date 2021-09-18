package com.example.myapplication.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.TableLayout;
import ohos.agp.utils.TextTool;
import ohos.app.Context;

import java.util.Locale;

/**
 * The GridView， a custom component like grid
 */
public class GridView extends TableLayout implements Component.LayoutRefreshedListener {
    private OnItemClickListener onItemClickListener;
    private OnRefreshedListener onRefreshedListener;

    private GridAdapter adapter;

    // custom Attr
    private int columnMargin;
    private int rowMargin;

    private boolean isOnRefreshed = false;

    public GridView(Context context) {
        super(context);
        setLayoutRefreshedListener(this);
    }

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        setLayoutRefreshedListener(this);
        columnMargin =
                attrSet.getAttr("columnMargin").isPresent()
                        ? attrSet.getAttr("columnMargin").get().getDimensionValue()
                        : 0;

        rowMargin =
                attrSet.getAttr("rowMargin").isPresent() ? attrSet.getAttr("rowMargin").get().getDimensionValue() : 0;
    }

    public GridView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        setLayoutRefreshedListener(this);
        columnMargin =
                attrSet.getAttr("columnMargin").isPresent()
                        ? attrSet.getAttr("columnMargin").get().getDimensionValue()
                        : 0;
        rowMargin =
                attrSet.getAttr("rowMargin").isPresent() ? attrSet.getAttr("rowMargin").get().getDimensionValue() : 0;
    }

    @Override
    public void onRefreshed(Component component) {
        if (isOnRefreshed) {
            return;
        }
        isOnRefreshed = true;

        layoutAdapter();
        if (onRefreshedListener != null) {
            onRefreshedListener.onRefreshed(component);
        }
    }

    /**
     * The setAdapter
     *
     * @param adapter adapter
     */
    public void setAdapter(GridAdapter adapter) {
        this.adapter = adapter;
        isOnRefreshed = false;
    }

    private void layoutAdapter() {
        removeAllComponents();

        for (int i = 0; i < adapter.getComponentList().size(); i++) {
            Component componentItem = adapter.getComponentList().get(i);

            ComponentContainer.LayoutConfig configComponent = componentItem.getLayoutConfig();
            int totalWidth = getWidth() - getPaddingStart() - getPaddingEnd();
            int columnMarginTmp = columnMargin;
            if (columnMargin == 0) {
                columnMarginTmp = (totalWidth - componentItem.getWidth() * getColumnCount()) / (getColumnCount() - 1);
            } else {
                configComponent.width = (totalWidth - columnMargin * (getColumnCount() - 1)) / getColumnCount();
                componentItem.setLayoutConfig(configComponent);
            }

            if (i % getColumnCount() != 0) {
                if (TextTool.isLayoutRightToLeft(Locale.getDefault())) {
                    componentItem.setMarginRight(columnMarginTmp);
                } else {
                    componentItem.setMarginLeft(columnMarginTmp);
                }
            }

            if (i / getColumnCount() != 0) {
                componentItem.setMarginTop(rowMargin);
            }

            final int index = i;

            componentItem.setClickedListener(
                    component -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onclick(component, index);
                        }
                    });

            addComponent(componentItem);
        }
    }

    /**
     * The setOnItemClickListener
     *
     * @param onItemClickListener onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * The setOnRefreshedListener
     *
     * @param onRefreshedListener onRefreshedListener
     */
    public void setOnRefreshedListener(OnRefreshedListener onRefreshedListener) {
        this.onRefreshedListener = onRefreshedListener;
    }

    /**
     * The OnItemClickListener
     */
    public static class OnItemClickListener {
        /**
         * The onclick
         *
         * @param component component
         * @param index     index
         */
        public void onclick(Component component, int index) {
        }
    }

    /**
     * The onRefreshedListener
     */
    public static class OnRefreshedListener {
        /**
         * The onRefreshed
         *
         * @param component component
         */
        public void onRefreshed(Component component) {
        }
    }
}

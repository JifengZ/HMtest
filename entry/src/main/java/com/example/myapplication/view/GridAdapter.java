package com.example.myapplication.view;

import com.example.myapplication.utils.LogUtils;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * The GridAdapter, adapter of the gridView
 */
public class GridAdapter {
    private final List<Component> componentList = new ArrayList<>();

    /**
     * The GridAdapter, adapter of the gridView
     *
     * @param context context
     * @param xmlId   xmlId, the xml layout of each item of gridView
     * @param data    data, the number of key-value also the number of item
     * @param from    from, the collection for the key in de data
     * @param to      to, the value from the data to the component target
     */
    public GridAdapter(Context context, int xmlId, List<Map<String, Object>> data, String[] from, int[] to) {
        for (Map<String, Object> datum : data) {
            // get component for the xml
            Component layoutComponent = LayoutScatter.getInstance(context).parse(xmlId, null, false);

            // create each itemComponent for the args：data、from、to
            IntStream.range(0, to.length)
                    .forEach(
                            index -> {
                                Component itemComponent = layoutComponent.findComponentById(to[index]);
                                if (itemComponent instanceof Image) {
                                    Image image = (Image) itemComponent;
                                    if (datum.get(from[index]) instanceof int[]) {
                                        try {
                                            image.setPixelMap(((int[]) datum.get(from[index]))[index]);
                                        } catch (IndexOutOfBoundsException e) {
                                            LogUtils.error("GridAdapter", "IndexOutOfBoundsException");
                                        }
                                    }
                                } else {
                                    if (itemComponent instanceof Text) {
                                        Text text = (Text) itemComponent;
                                        if (datum.get(from[index]) instanceof String[]) {
                                            try {
                                                text.setText(((String[]) datum.get(from[index]))[index]);
                                            } catch (IndexOutOfBoundsException e) {
                                                LogUtils.error("GridAdapter", "IndexOutOfBoundsException");
                                                text.setText("null");
                                            }
                                        } else if (datum.get(from[index]) instanceof int[]) {
                                            try {
                                                text.setText(((int[]) datum.get(from[index]))[index]);
                                            } catch (IndexOutOfBoundsException e) {
                                                LogUtils.error("GridAdapter", "IndexOutOfBoundsException");
                                                text.setText("null");
                                            }
                                        } else {
                                            text.setText("null");
                                        }
                                    }
                                }
                            });

            componentList.add(layoutComponent);
        }
    }

    /**
     * The getComponentList
     *
     * @return componentList
     */
    List<Component> getComponentList() {
        return componentList;
    }
}

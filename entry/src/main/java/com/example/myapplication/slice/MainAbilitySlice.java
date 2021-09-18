package com.example.myapplication.slice;

import com.example.myapplication.MainAbility;
import com.example.myapplication.ResourceTable;
import com.example.myapplication.data.CategoryData;
import com.example.myapplication.model.Item;
import com.example.myapplication.model.ItemChild;
import com.example.myapplication.provider.CategoryListProvider;
import com.example.myapplication.utils.ElementUtil;
import com.example.myapplication.utils.Toast;
import com.example.myapplication.view.GridAdapter;
import com.example.myapplication.view.GridView;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.TextAlignment;
import ohos.agp.utils.TextTool;
import ohos.global.configuration.Configuration;
import ohos.multimodalinput.event.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * The MainAbilitySlice, the first page of app
 */
public class MainAbilitySlice extends AbilitySlice {
    // Button number on the bottom
    private static final int BOTTOM_TAB_BUTTON_NUM = 4;

    private CategoryData categoryData;

    private CategoryListProvider categoryListProvider;
    private TextField searchTextField;
    private ListContainer categoryList;
    private ScrollView itemListScroll;
    private GridView itemLayoutGrid;
    private DirectionalLayout itemChildLayout;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        this.getWindow().setStatusBarColor(ElementUtil.getColor(this, ResourceTable.Color_colorSubBackground));
        this.getWindow().setNavigationBarColor(ElementUtil.getColor(this, ResourceTable.Color_colorSubBackground));

        categoryData = new CategoryData(this);

        initView();
        initListener();
        initBottomTab();
    }

    /**
     * The initView, get component from xml
     */
    private void initView() {
        // Init component
        searchTextField = (TextField) findComponentById(ResourceTable.Id_searchTextField);
        categoryList = (ListContainer) findComponentById(ResourceTable.Id_categoryList);
        itemListScroll = (ScrollView) findComponentById(ResourceTable.Id_itemListScroll);
        itemLayoutGrid = (GridView) findComponentById(ResourceTable.Id_itemLayoutGrid);
        itemChildLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_itemChildLayout);

        searchTextField.setTextAlignment(TextAlignment.START | TextAlignment.VERTICAL_CENTER);
        if (TextTool.isLayoutRightToLeft(Locale.getDefault())) {
            searchTextField.setLayoutDirection(Component.LayoutDirection.RTL);
        } else {
            searchTextField.setLayoutDirection(Component.LayoutDirection.LTR);
        }
    }

    private void initLocaleChange() {
        ((MainAbility) getAbility())
                .setLocaleChangeListener(
                        new MainAbility.LocaleChangeListener() {
                            @Override
                            public void onLocaleChange(Configuration configuration) {
                                super.onLocaleChange(configuration);
                                if (!configuration
                                        .getFirstLocale()
                                        .getLanguage()
                                        .equals(Locale.getDefault().getLanguage())) {
                                    categoryData = new CategoryData(MainAbilitySlice.this);
                                    int index = categoryListProvider.getSelectIndex();
                                    categoryListProvider =
                                            new CategoryListProvider(
                                                    categoryData.getCategoryList().getListText(),
                                                    MainAbilitySlice.this);
                                    categoryList.setItemProvider(categoryListProvider);
                                    categoryListProvider.setSelectIndex(index);

                                    refreshItemGrid(
                                            categoryData
                                                    .getItemList(
                                                            categoryData.getCategoryList().getListText().get(index))
                                                    .getListItem());
                                    initItemChild(
                                            categoryData
                                                    .getItemList(
                                                            categoryData.getCategoryList().getListText().get(index))
                                                    .getListItemChild());

                                    categoryListProvider.notifyDataChanged();
                                }
                            }
                        });
    }

    /**
     * The initListener, set listener of component
     */
    private void initListener() {
        initLocaleChange();

        searchTextField.setKeyEventListener(
                (component, keyEvent) -> {
                    if (keyEvent.isKeyDown() && keyEvent.getKeyCode() == KeyEvent.KEY_ENTER) {
                        Toast.makeToast(
                                MainAbilitySlice.this,
                                getString(ResourceTable.String_you_search) + searchTextField.getText(),
                                Toast.TOAST_SHORT)
                                .show();
                    }
                    return false;
                });

        // Init categoryList
        categoryList.setItemClickedListener(
                (listContainer, component, index, l1) -> {
                    if (categoryListProvider.getSelectIndex() == index) {
                        return;
                    }

                    String categoryName = categoryData.getCategoryList().getListText().get(index);
                    categoryListProvider.setSelectIndex(index);

                    refreshItemGrid(categoryData.getItemList(categoryName).getListItem());

                    initItemChild(categoryData.getItemList(categoryName).getListItemChild());

                    categoryListProvider.notifyDataChanged();

                    itemListScroll.fluentScrollYTo(0);
                });

        categoryListProvider = new CategoryListProvider(categoryData.getCategoryList().getListText(), this);
        categoryList.setItemProvider(categoryListProvider);

        // Init itemGrid
        itemLayoutGrid.setOnItemClickListener(
                new GridView.OnItemClickListener() {
                    @Override
                    public void onclick(Component component, int index) {
                        String text = ((Text) component.findComponentById(ResourceTable.Id_itemGridPerText)).getText();
                        Toast.makeToast(
                                MainAbilitySlice.this,
                                getString(ResourceTable.String_you_clicked) + text,
                                Toast.TOAST_SHORT)
                                .show();
                    }
                });

        refreshItemGrid(categoryData.getItemList(categoryData.getCategoryList().getListText().get(0)).getListItem());
        initItemChild(categoryData.getItemList(categoryData.getCategoryList().getListText().get(0)).getListItemChild());
    }

    /**
     * The refreshItemGrid, refresh the itemGrid each time when select a category
     *
     * @param itemList itemList, the bean of itemGrid
     */
    private void refreshItemGrid(List<Item> itemList) {
        List<Map<String, Object>> adapterData = new ArrayList<>();

        // Create adapterData
        IntStream.range(0, itemList.size()).forEach(index -> {
            Map<String, Object> showItem = new HashMap<>();
            String[] itemText = new String[]{itemList.get(index).getItemText()};
            showItem.put("itemGridPerText", itemText);
            adapterData.add(showItem);
        });

        // Create gridAdapter
        GridAdapter gridAdapter =
                new GridAdapter(
                        this,
                        ResourceTable.Layout_item_grid_per,
                        adapterData,
                        new String[]{"itemGridPerText"},
                        new int[]{ResourceTable.Id_itemGridPerText});

        // Set adapter
        itemLayoutGrid.setAdapter(gridAdapter);
    }

    /**
     * The initItemChild, init the itemChild each time when select a category
     *
     * @param itemChildList itemChildList, the bean of itemChild
     */
    private void initItemChild(List<ItemChild> itemChildList) {
        itemChildLayout.removeAllComponents();

        // Create itemChild
        for (ItemChild itemChild : itemChildList) {
            Component childComponent =
                    LayoutScatter.getInstance(this).parse(ResourceTable.Layout_item_child_per, null, false);

            Text text = (Text) childComponent.findComponentById(ResourceTable.Id_itemChildPerText);
            Text more = (Text) childComponent.findComponentById(ResourceTable.Id_itemChildPerMore);
            GridView gridView = (GridView) childComponent.findComponentById(ResourceTable.Id_itemChildPerGrid);

            gridView.setOnItemClickListener(
                    new GridView.OnItemClickListener() {
                        @Override
                        public void onclick(Component component, int index) {
                            String text =
                                    ((Text) component.findComponentById(ResourceTable.Id_itemChildPerGridPerText))
                                            .getText();
                            Toast.makeToast(
                                    MainAbilitySlice.this,
                                    getString(ResourceTable.String_you_clicked) + text,
                                    Toast.TOAST_SHORT)
                                    .show();
                        }
                    });

            gridView.setOnRefreshedListener(
                    new GridView.OnRefreshedListener() {
                        @Override
                        public void onRefreshed(Component component) {
                            text.setText(itemChild.getItemChildText());
                            more.setVisibility(Component.VISIBLE);
                        }
                    });

            List<Map<String, Object>> adapterData = new ArrayList<>();

            // Create adapterData
            for (int j = 0; j < itemChild.getListItem().size(); j++) {
                Map<String, Object> showitem = new HashMap<>();
                String[] itemText = new String[]{itemChild.getListItem().get(j).getItemText()};
                showitem.put("itemChildPerGridPerText", itemText);
                adapterData.add(showitem);
            }

            // Create gridAdapter
            GridAdapter gridAdapter =
                    new GridAdapter(
                            this,
                            ResourceTable.Layout_item_child_per_grid_per,
                            adapterData,
                            new String[]{"itemChildPerGridPerText"},
                            new int[]{ResourceTable.Id_itemChildPerGridPerText});

            // set adapter
            gridView.setAdapter(gridAdapter);

            itemChildLayout.addComponent(childComponent);
        }
    }

    /**
     * The initBottomTab, init the bottomTab
     */
    private void initBottomTab() {
        DirectionalLayout bottomTab = (DirectionalLayout) findComponentById(ResourceTable.Id_bottom_tabMenu);
        List<DirectionalLayout> tabList = new ArrayList<>();
        IntStream.range(0, BOTTOM_TAB_BUTTON_NUM).forEach(count -> {
            // Use LayoutScatter to convert xml file into Component instance
            DirectionalLayout tab =
                    (DirectionalLayout)
                            LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_tab, bottomTab, false);
            Image buttonImage = (Image) tab.findComponentById(ResourceTable.Id_bottom_tab_button_image);
            if (buttonImage != null) {
                buttonImage.setScaleMode(Image.ScaleMode.STRETCH);
                if (count == BOTTOM_TAB_BUTTON_NUM - 1) {
                    buttonImage.setPixelMap(ResourceTable.Media_icon_actived);
                } else {
                    buttonImage.setPixelMap(ResourceTable.Media_icon_normal);
                }
            }
            Text buttonText = (Text) tab.findComponentById(ResourceTable.Id_bottom_tab_button_text);
            if (buttonText != null) {
                buttonText.setText(getString(ResourceTable.String_tab));
            }
            tab.setClickedListener(
                    component -> {
                        // Deselect all tabs in tab menu
                        for (DirectionalLayout btn : tabList) {
                            ((Image) btn.findComponentById(ResourceTable.Id_bottom_tab_button_image))
                                    .setPixelMap(ResourceTable.Media_icon_normal);
                        }

                        // Set seleted state on the clicked tab
                        ((Image) component.findComponentById(ResourceTable.Id_bottom_tab_button_image))
                                .setPixelMap(ResourceTable.Media_icon_actived);
                    });
            tabList.add(tab);
            bottomTab.addComponent(tab);
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}

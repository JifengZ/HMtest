package com.example.myapplication.utils;

import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ElementUtil
 */
public class ElementUtil {
    /**
     * The getColor
     *
     * @param context    context
     * @param resColorId resColorId
     * @return color
     */
    public static int getColor(Context context, int resColorId) {
        try {
            String strColor = context.getResourceManager().getElement(resColorId).getString();
            if (strColor.length() == 7) {
                return context.getResourceManager().getElement(resColorId).getColor();
            } else if (strColor.length() == 9) {
                return Color.getIntColor(strColor);
            } else {
                return 0x000000;
            }
        } catch (WrongTypeException | NotExistException | IOException e) {
            Logger.getLogger(ElementUtil.class.getName()).log(Level.SEVERE, e.getMessage());
        }
        return 0x000000;
    }
}

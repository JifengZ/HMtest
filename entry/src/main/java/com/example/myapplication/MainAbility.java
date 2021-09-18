package com.example.myapplication;

import com.example.myapplication.slice.MainAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.global.configuration.Configuration;

public class MainAbility extends Ability {
    private LocaleChangeListener localeChangeListener;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }


    @Override
    public void onConfigurationUpdated(Configuration configuration) {
        super.onConfigurationUpdated(configuration);
        localeChangeListener.onLocaleChange(configuration);
    }

    public void setLocaleChangeListener(LocaleChangeListener localeChangeListener) {
        this.localeChangeListener = localeChangeListener;
    }

    /**
     * The LocaleChangeListener
     */
    public static class LocaleChangeListener {
        /**
         * The onLocaleChange
         *
         * @param configuration configuration
         */
        public void onLocaleChange(Configuration configuration) {
        }
    }
}

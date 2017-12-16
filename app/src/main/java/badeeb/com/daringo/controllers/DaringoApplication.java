package badeeb.com.daringo.controllers;

import android.app.Application;

import badeeb.com.daringo.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by meldeeb on 11/30/17.
 */

public class DaringoApplication extends Application{
    private static DaringoApplication sDaringoApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sDaringoApplication = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/opensans-regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static DaringoApplication getInstance() {
        if (sDaringoApplication == null) {
            sDaringoApplication = new DaringoApplication();
        }
        return sDaringoApplication;
    }
}
package badeeb.com.daringo.controllers;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import badeeb.com.daringo.R;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by meldeeb on 11/30/17.
 */

public class DaringoApplication extends Application{
    private static DaringoApplication sDaringoApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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

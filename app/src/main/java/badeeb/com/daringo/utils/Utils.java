package badeeb.com.daringo.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by meldeeb on 12/9/17.
 */

public class Utils {

    public static void clearFragmentsBackStack(FragmentManager fm){
        for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }
}

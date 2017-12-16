package badeeb.com.daringo.utils;

/**
 * Created by meldeeb on 12/6/17.
 */

public class FacebookUtils {

    public static String getImageUrl(String id) {
        return "https://graph.facebook.com/" + id + "/picture?type=normal";
    }
}

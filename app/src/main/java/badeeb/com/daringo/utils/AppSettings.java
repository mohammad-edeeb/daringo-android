package badeeb.com.daringo.utils;

/**
 * Created by meldeeb on 12/1/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import badeeb.com.daringo.R;
import badeeb.com.daringo.controllers.DaringoApplication;
import badeeb.com.daringo.models.User;


/**
 * Created by meldeeb on 9/25/17.
 */

public class AppSettings {

    private final static String PREF_FCM_TOKEN = "PREF_FCM_TOKEN";
    private final static String PREF_USER_ID = "PREF_USER_ID";
    private final static String PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME";
    private final static String PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME";
    private final static String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    private final static String PREF_USER_TOKEN = "PREF_USER_TOKEN";
    private final static String PREF_USER_IMAGE_URL = "PREF_USER_IMAGE_URL";
    private final static String PREF_USER_ACCOUNT_TYPE = "PREF_USER_ACCOUNT_TYPE";
    private final static String PREF_USER_SOCIAL_ACCOUNT_ID = "PREF_USER_SOCIAL_ACCOUNT_ID";
    private final static String PREF_USER_SOCIAL_ACCOUNT_TOKEN = "PREF_USER_SOCIAL_ACCOUNT_TOKEN";

    private static AppSettings sInstance;

    private SharedPreferences sPreferences;

    public static AppSettings getInstance() {
        if (sInstance == null) {
            sInstance = new AppSettings(DaringoApplication.getInstance());
        }
        return sInstance;
    }


    private AppSettings(Context context) {
        String fileName = context.getString(R.string.app_name);
        this.sPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    private void putValue(String key, String value) {
        sPreferences.edit().putString(key, value).commit();
    }

    private String getValue(String key, String defaultValue) {
        return sPreferences.getString(key, defaultValue);
    }

    private void putValue(String key, int value) {
        sPreferences.edit().putInt(key, value).commit();
    }

    private int getValue(String key, int defaultValue) {
        return sPreferences.getInt(key, defaultValue);
    }

    private void putValue(String key, double value) {
        sPreferences.edit().putString(key, value + "").commit();
    }

    private double getValue(String key, double defaultValue) {
        return Double.parseDouble(sPreferences.getString(key, defaultValue + ""));
    }

    public boolean isLoggedIn() {
        String authenticationToken = getUserToken();
        return !TextUtils.isEmpty(authenticationToken);
    }

    public User getUser() {
        User user = new User();
        user.setId(getUserId());
        user.setEmail(getUserEmail());
        user.setFirstName(getUserFirstName());
        user.setLastName(getUserLastName());
        user.setToken(getUserToken());
        user.setImageUrl(getUserImageUrl());
        user.setAccountType(getUserAccountType());
        user.setSocialAccountId(getUserSocialAccountId());
        user.setSocialAccountToken(getUserSocialAccountToken());
        user.setFcmToken(getFcmToken());
        return user;
    }

    public void saveUser(User user) {
        setUserId(user.getId());
        setUserFirstName(user.getFirstName());
        setUserLastName(user.getLastName());
        setUserEmail(user.getEmail());
        setUserImageUrl(user.getImageUrl());
        setUserToken(user.getToken());
        setUserAccountType(user.getAccountType());
        setUserSocialAccountId(user.getSocialAccountId());
        setUserSocialAccountToken(user.getSocialAccountToken());
        setFcmToken(user.getFcmToken());
    }

    public void setFcmToken(String fcmToken) {
        putValue(PREF_FCM_TOKEN, fcmToken);
    }

    public void setUserId(int userId) {
        putValue(PREF_USER_ID, userId);
    }

    public void setUserEmail(String prefEmail) {
        putValue(PREF_USER_EMAIL, prefEmail);
    }

    public void setUserFirstName(String prefFirstName) {
        putValue(PREF_USER_FIRST_NAME, prefFirstName);
    }

    public void setUserLastName(String prefLastName) {
        putValue(PREF_USER_LAST_NAME, prefLastName);
    }

    public void setUserImageUrl(String prefUserImageUrl) {
        putValue(PREF_USER_IMAGE_URL, prefUserImageUrl);
    }

    public void setUserToken(String prefUserToken) {
        putValue(PREF_USER_TOKEN, prefUserToken);
    }

    public void setUserAccountType(String userAccountType) {
        putValue(PREF_USER_ACCOUNT_TYPE, userAccountType);
    }

    public void setUserSocialAccountId(String userSocialAccountId){
        putValue(PREF_USER_SOCIAL_ACCOUNT_ID, userSocialAccountId);
    }

    public void setUserSocialAccountToken(String userSocialAccountToken){
        putValue(PREF_USER_SOCIAL_ACCOUNT_TOKEN, userSocialAccountToken);
    }

    public String getFcmToken() {
        return getValue(PREF_FCM_TOKEN, "");
    }

    public int getUserId() {
        return getValue(PREF_USER_ID, 0);
    }

    public String getUserToken() {
        return getValue(PREF_USER_TOKEN, "");
    }

    public String getUserEmail() {
        return getValue(PREF_USER_EMAIL, "");
    }

    public String getUserImageUrl() {
        return getValue(PREF_USER_IMAGE_URL, "");
    }

    public String getUserFirstName() {
        return getValue(PREF_USER_FIRST_NAME, "");
    }

    public String getUserLastName() {
        return getValue(PREF_USER_LAST_NAME, "");
    }

    public String getUserAccountType() {
        return getValue(PREF_USER_ACCOUNT_TYPE, "");
    }

    public String getUserSocialAccountId() {
        return getValue(PREF_USER_SOCIAL_ACCOUNT_ID, "");
    }

    public String getUserSocialAccountToken() {
        return getValue(PREF_USER_SOCIAL_ACCOUNT_TOKEN, "");
    }

    // don't remove fcm token here
    public void clearUserInfo() {
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.remove(PREF_USER_ID)
                .remove(PREF_USER_EMAIL)
                .remove(PREF_USER_IMAGE_URL)
                .remove(PREF_USER_TOKEN)
                .remove(PREF_USER_FIRST_NAME)
                .remove(PREF_USER_LAST_NAME)
                .remove(PREF_USER_ACCOUNT_TYPE)
                .remove(PREF_USER_SOCIAL_ACCOUNT_ID)
                .remove(PREF_USER_SOCIAL_ACCOUNT_TOKEN);
        editor.commit();
    }

}


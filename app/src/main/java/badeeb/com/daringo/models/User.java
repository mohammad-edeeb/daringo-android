package badeeb.com.daringo.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by meldeeb on 11/30/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class User {

    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    private String token;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("account_type")
    private String accountType;
    @SerializedName("social_account_id")
    private String socialAccountId;
    @SerializedName("social_account_token")
    private String socialAccountToken;
    @SerializedName("fcm_token")
    private String fcmToken;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName(){
        String result = getFirstName();
        if(!TextUtils.isEmpty(getLastName())){
            result += " " + getLastName();
        }
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getSocialAccountId() {
        return socialAccountId;
    }

    public void setSocialAccountId(String socialAccountId) {
        this.socialAccountId = socialAccountId;
    }

    public String getSocialAccountToken() {
        return socialAccountToken;
    }

    public void setSocialAccountToken(String socialAccountToken) {
        this.socialAccountToken = socialAccountToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

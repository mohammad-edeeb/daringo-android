package badeeb.com.daringo.models.requests;

import com.google.gson.annotations.SerializedName;

import badeeb.com.daringo.models.User;

/**
 * Created by meldeeb on 12/1/17.
 */

public class SocialLoginRequest {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

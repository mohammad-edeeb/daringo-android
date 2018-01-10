package badeeb.com.daringo.models.requests;

import com.google.gson.annotations.SerializedName;

import badeeb.com.daringo.models.User;

/**
 * Created by meldeeb on 1/9/18.
 */

public class UpdateFcmTokenRequest {
    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

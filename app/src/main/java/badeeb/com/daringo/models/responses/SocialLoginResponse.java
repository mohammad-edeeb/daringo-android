package badeeb.com.daringo.models.responses;

import badeeb.com.daringo.models.User;

/**
 * Created by meldeeb on 12/9/17.
 */

public class SocialLoginResponse {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

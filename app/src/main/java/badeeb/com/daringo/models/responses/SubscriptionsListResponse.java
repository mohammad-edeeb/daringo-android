package badeeb.com.daringo.models.responses;

import java.util.List;

import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.Subscription;

/**
 * Created by meldeeb on 12/9/17.
 */

public class SubscriptionsListResponse {

    private Challenge challenge;
    private List<Subscription> subscriptions;

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}

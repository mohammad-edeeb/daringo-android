package badeeb.com.daringo.models.responses;

import badeeb.com.daringo.models.Subscription;

/**
 * Created by meldeeb on 12/9/17.
 */

public class SubscriptionDetailResponse {
    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}

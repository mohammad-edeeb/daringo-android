package badeeb.com.daringo.models.requests;

import badeeb.com.daringo.models.Challenge;

/**
 * Created by meldeeb on 12/9/17.
 */

public class CreateChallengeRequest {

    private Challenge challenge;

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}

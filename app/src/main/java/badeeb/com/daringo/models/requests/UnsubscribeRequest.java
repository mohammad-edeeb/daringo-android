package badeeb.com.daringo.models.requests;

import java.util.List;

import badeeb.com.daringo.models.Challenge;

/**
 * Created by meldeeb on 12/16/17.
 */

public class UnsubscribeRequest {

    private List<Challenge> challenges;

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}

package badeeb.com.daringo.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import badeeb.com.daringo.models.Challenge;

/**
 * Created by meldeeb on 12/3/17.
 */

public class ChallengesListResponse {

    @SerializedName("challenges")
    private List<Challenge> challenges;

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }
}

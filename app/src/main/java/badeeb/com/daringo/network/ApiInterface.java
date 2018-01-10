package badeeb.com.daringo.network;

import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.CreateChallengeRequest;
import badeeb.com.daringo.models.requests.UnsubscribeRequest;
import badeeb.com.daringo.models.requests.UpdateFcmTokenRequest;
import badeeb.com.daringo.models.requests.UpdateSubscriptionRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.ChallengesListResponse;
import badeeb.com.daringo.models.requests.SocialLoginRequest;
import badeeb.com.daringo.models.responses.CompleteBlockResponse;
import badeeb.com.daringo.models.responses.CreateChallengeResponse;
import badeeb.com.daringo.models.responses.SocialLoginResponse;
import badeeb.com.daringo.models.responses.SubscriptionDetailResponse;
import badeeb.com.daringo.models.responses.SubscriptionsListResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by meldeeb on 11/30/17.
 */

public interface ApiInterface {

    @POST("users/social_login")
    Call<BaseResponse<SocialLoginResponse>> socialLogin(@Body BaseRequest<SocialLoginRequest> request);

    @PUT("users/update_fcm_token")
    Call<BaseResponse<EmptyResponse>> updateFcmToken(@Body BaseRequest<UpdateFcmTokenRequest> request);

    @GET("challenges")
    Call<BaseResponse<ChallengesListResponse>> getChallengesList();

    @GET("challenges/ended")
    Call<BaseResponse<ChallengesListResponse>> getPastChallengesList();

    @POST("challenges")
    Call<BaseResponse<CreateChallengeResponse>> createChallenge(
            @Body BaseRequest<CreateChallengeRequest> request);

    @POST("challenges/unsubscribe")
    Call<BaseResponse<EmptyResponse>> unsubscribe(
            @Body BaseRequest<UnsubscribeRequest> request);

    @GET("challenges/{challengeId}/subscriptions")
    Call<BaseResponse<SubscriptionsListResponse>> getChallengeSubscriptions(
            @Path("challengeId") int challengeId);

    @POST("challenges/{challengeId}/complete")
    Call<BaseResponse<EmptyResponse>> completeChallenge(
            @Path("challengeId") int challengeId);

    @GET("challenges/{challengeId}/subscriptions/{subscriptionId}")
    Call<BaseResponse<SubscriptionDetailResponse>> getSubscriptionDetail(
            @Path("challengeId") int challengeId, @Path("subscriptionId") int subscriptionId);

    @PATCH("challenges/{challengeId}/subscriptions/{subscriptionId}")
    Call<BaseResponse<EmptyResponse>> updateSubscription(
            @Body BaseRequest<UpdateSubscriptionRequest> request,
            @Path("challengeId") int challengeId,
            @Path("subscriptionId") int subscriptionId);

    @POST("challenges/{challengeId}/subscriptions/{subscriptionId}/blocks/{blockId}/toggle")
    Call<BaseResponse<CompleteBlockResponse>> toggleBlock(
            @Path("challengeId") int challengeId,
            @Path("subscriptionId") int subscriptionId,
            @Path("blockId") int blockId);

    @DELETE("users")
    Call<BaseResponse<EmptyResponse>> logout();

}

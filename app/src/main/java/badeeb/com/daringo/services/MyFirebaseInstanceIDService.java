package badeeb.com.daringo.services;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.parceler.Parcels;

import badeeb.com.daringo.activities.LoginActivity;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.SocialLoginRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.SocialLoginResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.AppSettings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meldeeb on 12/14/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        handleNewToken(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void handleNewToken(String token) {
        AppSettings settings = AppSettings.getInstance();
        if(settings.isLoggedIn()){
            callSocialLoginApi(token);
        } else {
            settings.setFcmToken(token);
        }
    }

    private void callSocialLoginApi(String token) {
        final AppSettings settings = AppSettings.getInstance();
        User user = settings.getUser();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(false)
                .create(ApiInterface.class);

        SocialLoginRequest request = new SocialLoginRequest();
        request.setUser(user);

        final Call<BaseResponse<SocialLoginResponse>> socialLoginResponse = apiService.socialLogin(new BaseRequest<SocialLoginRequest>(request));
        socialLoginResponse.enqueue(new Callback<BaseResponse<SocialLoginResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SocialLoginResponse>> call, Response<BaseResponse<SocialLoginResponse>> response) {
                if (response.code() == 200) {
                    User user = response.body().getData().getUser();
                    settings.saveUser(user);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SocialLoginResponse>> call, Throwable t) {
            }
        });
    }
}

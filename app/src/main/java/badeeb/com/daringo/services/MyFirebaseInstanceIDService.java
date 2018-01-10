package badeeb.com.daringo.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.SocialLoginRequest;
import badeeb.com.daringo.models.requests.UpdateFcmTokenRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
import badeeb.com.daringo.models.responses.SocialLoginResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.AppSettings;
import badeeb.com.daringo.utils.Utils;
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

        handleNewToken();
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     */
    private void handleNewToken() {
        AppSettings settings = AppSettings.getInstance();
        settings.setFcmTokenSaved(false);
        if(settings.isLoggedIn()){
            Utils.callUpdateFcmTokenApi();
        }
    }
}

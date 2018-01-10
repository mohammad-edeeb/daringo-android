package badeeb.com.daringo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Arrays;

import badeeb.com.daringo.R;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.requests.SocialLoginRequest;
import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.responses.SocialLoginResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.AppSettings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends CustomFontActivity {

    private static final int FB_REQUEST_CODE = 64206;

    private Button bLoginByFacebook;

    private LoginManager mLoginManager;
    private CallbackManager mFacebookCallbackManager;
    private AppSettings settings;

    private boolean fcmTokenSentToUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show view on top of status bar + transparent status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);

        AccessToken.getCurrentAccessToken();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        bLoginByFacebook = (Button) findViewById(R.id.bLoginByFacebook);

        bLoginByFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareFacebookLogin();
            }
        });

        settings = AppSettings.getInstance();
        User loggedInUser = settings.getUser();

        if(!TextUtils.isEmpty(loggedInUser.getToken())){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.EXTRA_CURRENT_USER, Parcels.wrap(loggedInUser));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FB_REQUEST_CODE) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void prepareFacebookLogin() {
        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();

        mLoginManager.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                System.out.println("canceled");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("error");
            }
        });

        mLoginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));

    }

    private void graphRequest(final AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                User u = new User();
                try {
                    u.setAccountType("facebook");
                    u.setFirstName(object.getString("first_name"));
                    u.setLastName(object.getString("last_name"));
                    u.setEmail(object.getString("email"));
                    u.setImageUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                    u.setSocialAccountId(object.getString("id"));
                    u.setSocialAccountToken(token.getToken());
                    if(FirebaseInstanceId.getInstance().getToken() != null){
                        u.setFcmToken(FirebaseInstanceId.getInstance().getToken());
                        fcmTokenSentToUpdate = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callSocialLoginApi(u);

            }
        });

        Bundle b = new Bundle();
        b.putString("fields", "id,email,first_name,last_name,picture.type(large)");
        request.setParameters(b);
        request.executeAsync();

    }

    private void callSocialLoginApi(User user) {
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
                    if(fcmTokenSentToUpdate){
                        settings.setFcmTokenSaved(true);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_CURRENT_USER, Parcels.wrap(user));
                    LoginActivity.this.startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SocialLoginResponse>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

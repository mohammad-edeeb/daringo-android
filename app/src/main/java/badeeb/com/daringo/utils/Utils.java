package badeeb.com.daringo.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.UpdateFcmTokenRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
import badeeb.com.daringo.models.responses.ErrorResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by meldeeb on 12/9/17.
 */

public class Utils {

    public static void clearFragmentsBackStack(FragmentManager fm){
        for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    public static ErrorResponse parseErrorResponse(retrofit2.Response response){
        Gson gson = new GsonBuilder().create();
        ErrorResponse result = null;
        try {
            result = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void callUpdateFcmTokenApi() {
        User user = new User();
        user.setFcmToken(FirebaseInstanceId.getInstance().getToken());

        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UpdateFcmTokenRequest request = new UpdateFcmTokenRequest();
        request.setUser(user);

        final Call<BaseResponse<EmptyResponse>> updateFcmTokenResponse = apiService
                .updateFcmToken(new BaseRequest<>(request));

        updateFcmTokenResponse.enqueue(new Callback<BaseResponse<EmptyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<EmptyResponse>> call, retrofit2.Response<BaseResponse<EmptyResponse>> response) {
                if (response.code() == 200) {
                    AppSettings settings = AppSettings.getInstance();
                    settings.setFcmTokenSaved(true);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<EmptyResponse>> call, Throwable t) {
            }
        });
    }
}

package badeeb.com.daringo.network;

/**
 * Created by meldeeb on 12/1/17.
 */

import badeeb.com.daringo.utils.AppSettings;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;


import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static final String BASE_URL = "https://staging-daringo.herokuapp.com/api/v1/";
    public static Retrofit retrofit = null;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    public Retrofit getClient(boolean authorized) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getHeader(authorized))
                .build();

        return retrofit;
    }

    public OkHttpClient getHeader(final boolean authorized) {
        OkHttpClient okClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest
                        .newBuilder()
                        .header("Accept", "application/json");

                if(authorized){
                    AppSettings settings = AppSettings.getInstance();
                    builder.header("Authorization", settings.getUserToken());
                }

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();
        return okClient;
    }
}
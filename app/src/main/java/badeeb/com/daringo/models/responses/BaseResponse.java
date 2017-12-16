package badeeb.com.daringo.models.responses;

import com.google.gson.annotations.SerializedName;

import badeeb.com.daringo.models.ResponseMeta;

/**
 * Created by meldeeb on 11/30/17.
 */

public class BaseResponse<T> {

    @SerializedName("meta")
    private ResponseMeta meta;
    @SerializedName("data")
    private T data;

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
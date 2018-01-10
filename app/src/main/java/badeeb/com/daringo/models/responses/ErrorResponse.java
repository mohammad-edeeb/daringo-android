package badeeb.com.daringo.models.responses;

import com.google.gson.annotations.SerializedName;

import badeeb.com.daringo.models.ResponseMeta;

/**
 * Created by meldeeb on 12/28/17.
 */

public class ErrorResponse {
    @SerializedName("meta")
    private ResponseMeta meta;

    public ResponseMeta getMeta() {
        return meta;
    }

    public void setMeta(ResponseMeta meta) {
        this.meta = meta;
    }

}

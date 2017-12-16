package badeeb.com.daringo.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meldeeb on 11/30/17.
 */

public class BaseRequest<T> {

    @SerializedName("data")
    private T data;

    public BaseRequest(T body) {
        this.data = body;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

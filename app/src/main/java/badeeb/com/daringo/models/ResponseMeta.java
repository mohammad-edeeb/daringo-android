package badeeb.com.daringo.models;

/**
 * Created by meldeeb on 11/30/17.
 */

public class ResponseMeta {
    private String status;
    private String message;

    public ResponseMeta() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

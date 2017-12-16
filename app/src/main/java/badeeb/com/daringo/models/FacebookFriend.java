package badeeb.com.daringo.models;

/**
 * Created by meldeeb on 12/6/17.
 */

public class FacebookFriend {

    private String id;
    private String name;
    private String imageUrl;
    private boolean checked;

    public FacebookFriend(String id, String name, String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

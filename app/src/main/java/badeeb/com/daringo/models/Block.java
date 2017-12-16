package badeeb.com.daringo.models;

import org.parceler.Parcel;

/**
 * Created by meldeeb on 12/9/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Block {

    private int id;
    private String text;
    private boolean completed;
    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

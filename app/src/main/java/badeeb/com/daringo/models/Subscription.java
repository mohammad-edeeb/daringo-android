package badeeb.com.daringo.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by meldeeb on 12/9/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Subscription {

    private int id;
    private String condition;
    private User user;
    @SerializedName("num_of_completed_blocks")
    private int numOfCompletedBlocks;
    @SerializedName("can_edit")
    private boolean canEdit;
    private List<Block> blocks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public int getNumOfCompletedBlocks() {
        return numOfCompletedBlocks;
    }

    public void setNumOfCompletedBlocks(int numOfCompletedBlocks) {
        this.numOfCompletedBlocks = numOfCompletedBlocks;
    }
}

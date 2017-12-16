package badeeb.com.daringo.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by meldeeb on 12/1/17.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Challenge {

    private int id;
    private String title;
    private String description;
    @SerializedName("start_date")
    private Date startDate;
    @SerializedName("end_date")
    private Date endDate;
    @SerializedName("num_of_blocks")
    private int numOfBlocks;
    @SerializedName("num_of_completed_blocks")
    private int numOfCompletedBlocks;
    private boolean completed;
    private User owner;
    private User winner;
    private List<User> participants;
    private List<Subscription> subscriptions;

    private boolean highlighted;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getNumOfBlocks() {
        return numOfBlocks;
    }

    public void setNumOfBlocks(int numOfBlocks) {
        this.numOfBlocks = numOfBlocks;
    }

    public int getNumOfCompletedBlocks() {
        return numOfCompletedBlocks;
    }

    public void setNumOfCompletedBlocks(int numOfCompletedBlocks) {
        this.numOfCompletedBlocks = numOfCompletedBlocks;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}

package badeeb.com.daringo.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by meldeeb on 12/9/17.
 */

public class CompleteBlockResponse {

    @SerializedName("blocks_completed")
    private boolean blocksCompleted;

    public boolean isBlocksCompleted() {
        return blocksCompleted;
    }

    public void setBlocksCompleted(boolean blocksCompleted) {
        this.blocksCompleted = blocksCompleted;
    }
}

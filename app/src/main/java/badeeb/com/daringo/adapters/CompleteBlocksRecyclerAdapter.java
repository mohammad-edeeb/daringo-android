package badeeb.com.daringo.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import badeeb.com.daringo.R;
import badeeb.com.daringo.fragments.CompleteBlocksFragment;
import badeeb.com.daringo.models.Block;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.CompleteBlockResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.OnSwipeTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by meldeeb on 12/9/17.
 */

public class CompleteBlocksRecyclerAdapter extends BaseRecyclerAdapter<Block> {

    private CompleteBlocksFragment fragment;
    private Challenge challenge;
    private Subscription subscription;
    private boolean currentUserSubscription;

    public CompleteBlocksRecyclerAdapter(Context context, CompleteBlocksFragment fragment,
                                         Challenge challenge, Subscription subscription,
                                         boolean currentUserSubscription) {
        super(context);
        this.fragment = fragment;
        this.challenge = challenge;
        this.subscription = subscription;
        this.currentUserSubscription = currentUserSubscription;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_complete_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Block block = getItemAt(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvBlock.setText(block.getText());
        if (block.isCompleted()) {
            viewHolder.tvBlock.setTextColor(ContextCompat
                    .getColor(context, R.color.completed_block));
            viewHolder.tvBlock.setPaintFlags(viewHolder.tvBlock.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.tvBlock.setTextColor(ContextCompat
                    .getColor(context, R.color.not_completed_block));
            viewHolder.tvBlock.setPaintFlags(viewHolder.tvBlock.getPaintFlags()
                    & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        if (currentUserSubscription) {
            viewHolder.tvBlock.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeRight() {
                    if (!block.isCompleted()) {
                        toggleBlock(block, position);
                        callToggleBlockApi(position, block);
                    }
                }

                public void onSwipeLeft() {
                    if (block.isCompleted()) {
                        toggleBlock(block, position);
                        callToggleBlockApi(position, block);
                    }
                }
            });
        }
    }

    private void toggleBlock(Block block, int position) {
        block.setCompleted(!block.isCompleted());
        refreshItem(position);
        if (block.isCompleted()) {
            fragment.addCompletedBlock();
        } else {
            fragment.removeCompletedBlock();
        }
    }


    private void callToggleBlockApi(final int position, final Block block) {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        final Call<BaseResponse<CompleteBlockResponse>> response = apiService
                .toggleBlock(challenge.getId(), subscription.getId(), block.getId());

        response.enqueue(new Callback<BaseResponse<CompleteBlockResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CompleteBlockResponse>> call, Response<BaseResponse<CompleteBlockResponse>> response) {
                if (response.code() == 200) {
                    fragment.showFinishButton(response.body().getData().isBlocksCompleted());
                    String message = block.isCompleted() ? "Bet completed" : "Bet reverted";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    toggleBlock(block, position);
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CompleteBlockResponse>> call, Throwable t) {
                toggleBlock(block, position);
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBlock = itemView.findViewById(R.id.tvBlock);
        }
    }

}
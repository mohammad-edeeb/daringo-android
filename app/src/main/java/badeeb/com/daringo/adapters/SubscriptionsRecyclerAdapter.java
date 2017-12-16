package badeeb.com.daringo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import badeeb.com.daringo.R;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.models.User;
import badeeb.com.daringo.utils.AppSettings;
import badeeb.com.daringo.utils.UiUtils;

/**
 * Created by meldeeb on 12/8/17.
 */

public class SubscriptionsRecyclerAdapter extends BaseRecyclerAdapter<Subscription>{
    private Challenge challenge;

    public SubscriptionsRecyclerAdapter(Context context, Challenge challenge) {
        super(context);
        this.challenge = challenge;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Subscription subscription = getItemAt(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        AppSettings settings = AppSettings.getInstance();
        if(subscription.getUser().getId() == settings.getUserId()){
            viewHolder.tvParticipantName.setText("You");
        } else {
            viewHolder.tvParticipantName.setText(subscription.getUser().getName());
        }
        viewHolder.tvCompletedBlocks.setText(formatCompletedBlocks(subscription));
        Glide.with(context)
                .load(subscription.getUser().getImageUrl())
                .into(viewHolder.rivParticipantPhoto);
    }

    private String formatCompletedBlocks(Subscription subscription){
        return subscription.getNumOfCompletedBlocks() + "/" + challenge.getNumOfBlocks();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivParticipantPhoto;
        TextView tvParticipantName;
        TextView tvCompletedBlocks;

        public ViewHolder(View itemView) {
            super(itemView);
            rivParticipantPhoto = itemView.findViewById(R.id.rivParticipantPhoto);
            tvParticipantName = itemView.findViewById(R.id.tvParticipantName);
            tvCompletedBlocks = itemView.findViewById(R.id.tvCompletedBlocks);
        }
    }
}

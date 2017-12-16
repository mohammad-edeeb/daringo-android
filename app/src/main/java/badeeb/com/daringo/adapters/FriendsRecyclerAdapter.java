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
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.utils.UiUtils;

/**
 * Created by meldeeb on 12/6/17.
 */

public class FriendsRecyclerAdapter extends BaseRecyclerAdapter<FacebookFriend>{

    public FriendsRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_friend, parent, false);
        return new FriendsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FacebookFriend friend = getItemAt(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvFriendName.setText(friend.getName());
        Glide.with(context).load(friend.getImageUrl()).into(viewHolder.rivFriendPhoto);
        if(friend.isChecked()){
            UiUtils.show(viewHolder.ivChecked);
        } else {
            UiUtils.hide(viewHolder.ivChecked);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivFriendPhoto;
        ImageView ivChecked;
        TextView tvFriendName;

        public ViewHolder(View itemView) {
            super(itemView);
            rivFriendPhoto = itemView.findViewById(R.id.rivFriendPhoto);
            ivChecked = itemView.findViewById(R.id.ivChecked);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
        }
    }
}

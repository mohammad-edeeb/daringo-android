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

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.utils.UiUtils;

/**
 * Created by meldeeb on 12/6/17.
 */

public class PhotosHorizontalRecyclerAdapter extends BaseRecyclerAdapter<FacebookFriend>{

    public PhotosHorizontalRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FacebookFriend friend = getItemAt(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context).load(friend.getImageUrl()).into(viewHolder.rivFriendPhoto);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivFriendPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            rivFriendPhoto = itemView.findViewById(R.id.rivFriendPhoto);
        }
    }
}

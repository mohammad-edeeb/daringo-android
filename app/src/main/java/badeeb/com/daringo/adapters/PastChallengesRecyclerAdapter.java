package badeeb.com.daringo.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.utils.AppSettings;

/**
 * Created by meldeeb on 12/16/17.
 */

public class PastChallengesRecyclerAdapter extends BaseRecyclerAdapter<Challenge> {

    private boolean deleteMode = false;
    private List<Challenge> toBeDeleted;

    public PastChallengesRecyclerAdapter(Context mContext) {
        super(mContext);
        toBeDeleted = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_past_challenge, parent, false);
        return new ViewHolder(view);
    }

    public List<Challenge> getChallengesToBeDeleted(){
        return toBeDeleted;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final Challenge challenge = getItemAt(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                highlightChallenge(challenge, position);
                return true;
            }
        });

        ViewHolder viewHolder = (ViewHolder) holder;
        if (challenge.isHighlighted()) {
            holder.itemView.setBackgroundColor(ContextCompat
                    .getColor(context, R.color.challenge_highlighted));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat
                    .getColor(context, R.color.white));
        }
        viewHolder.tvChallengeTitle.setText(challenge.getTitle());
        viewHolder.tvWinnerStatus.setText(getWinnerStatus(challenge));
    }

    public void revertDeleteMode(){
        List<Challenge> temp = new ArrayList<>();
        temp.addAll(toBeDeleted);
        for (Challenge c: temp) {
            highlightChallenge(c, getItems().indexOf(c));
        }
    }

    public void clearToBeDeleted() {
        toBeDeleted.clear();
        deleteMode = false;
        ((MainActivity) context).onChallengeDeleteMode(deleteMode, false);
    }

    public void highlightChallenge(Challenge challenge, int position) {
        if (challenge.isHighlighted()) {
            toBeDeleted.remove(challenge);
        } else {
            toBeDeleted.add(challenge);
        }
        deleteMode = !toBeDeleted.isEmpty();
        ((MainActivity) context).onChallengeDeleteMode(deleteMode, false);
        // based on deleteMode hide/show the header delete button --> call activity
        challenge.setHighlighted(!challenge.isHighlighted());
        refreshItem(position);
    }

    private String getWinnerStatus(Challenge challenge) {
        if(challenge.getWinner() != null){
            AppSettings settings = AppSettings.getInstance();
            if(challenge.getWinner().getId() == settings.getUserId()){
                return "You won the challenge";
            }
            return challenge.getWinner().getName() + " won the challenge";
        }
        return "Challenge ended with no winner";
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChallengeTitle;
        TextView tvWinnerStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvChallengeTitle = (TextView) itemView.findViewById(R.id.tvChallengeTitle);
            tvWinnerStatus = (TextView) itemView.findViewById(R.id.tvWinnerStatus);
        }
    }

}

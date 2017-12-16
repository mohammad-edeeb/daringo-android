package badeeb.com.daringo.adapters;

/**
 * Created by meldeeb on 12/1/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.User;


public class ChallengesRecyclerAdapter extends BaseRecyclerAdapter<Challenge> {

    private boolean deleteMode = false;
    private List<Challenge> toBeDeleted;

    public ChallengesRecyclerAdapter(Context mContext) {
        super(mContext);
        toBeDeleted = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_challenge, parent, false);
        return new ViewHolder(view);
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
        viewHolder.tvChallengeParticipants.setText(participantsNames(challenge.getParticipants()));
        viewHolder.tvCompletedPredictions.setText(String.valueOf(challenge.getNumOfCompletedBlocks()));
    }

    public List<Challenge> getChallengesToBeDeleted(){
        return toBeDeleted;
    }

    public boolean isDeleteMode(){
        return deleteMode;
    }

    public void revertDeleteMode(){
        List<Challenge> temp = new ArrayList<>();
        temp.addAll(toBeDeleted);
        for (Challenge c: temp) {
            highlightChallenge(c, getItems().indexOf(c));
        }
    }

    public void highlightChallenge(Challenge challenge, int position) {
        if (challenge.isHighlighted()) {
            toBeDeleted.remove(challenge);
        } else {
            toBeDeleted.add(challenge);
        }
        deleteMode = !toBeDeleted.isEmpty();
        ((MainActivity) context).onChallengeDeleteMode(deleteMode);
        // based on deleteMode hide/show the header delete button --> call activity
        challenge.setHighlighted(!challenge.isHighlighted());
        refreshItem(position);
    }

    private String participantsNames(List<User> participants) {
        String result = "";
        List<String> names = new ArrayList<>();
        for (User user : participants) {
            names.add(user.getFirstName());
        }
        return TextUtils.join(", ", names);
    }

    public void clearToBeDeleted() {
        toBeDeleted.clear();
        deleteMode = false;
        ((MainActivity) context).onChallengeDeleteMode(deleteMode);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llMain;
        TextView tvChallengeTitle;
        TextView tvChallengeParticipants;
        TextView tvCompletedPredictions;

        public ViewHolder(View itemView) {
            super(itemView);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            tvChallengeTitle = (TextView) itemView.findViewById(R.id.tvChallengeTitle);
            tvChallengeParticipants = (TextView) itemView.findViewById(R.id.tvChallengeParticipants);
            tvCompletedPredictions = (TextView) itemView.findViewById(R.id.tvCompletedPredictions);
        }
    }

}

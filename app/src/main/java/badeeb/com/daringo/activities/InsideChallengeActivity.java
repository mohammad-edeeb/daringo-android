package badeeb.com.daringo.activities;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import badeeb.com.daringo.R;
import badeeb.com.daringo.fragments.CompleteBlocksFragment;
import badeeb.com.daringo.fragments.ParticipantsFragment;
import badeeb.com.daringo.fragments.EditBlocksFragment;
import badeeb.com.daringo.fragments.WinnerFragment;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.utils.DateUtils;
import badeeb.com.daringo.utils.Utils;

public class InsideChallengeActivity extends CustomFontActivity {

    public static final String EXTRA_CHALLENGE = "EXTRA_CHALLENGE";

    private FrameLayout flMainFrame;
    private TextView tvTimer;

    private Challenge challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_challenge);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button_copy_2);

        flMainFrame = findViewById(R.id.flMainFrame);
        tvTimer = findViewById(R.id.tvTimer);

        if (getIntent().getParcelableExtra(EXTRA_CHALLENGE) != null) {
            challenge = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CHALLENGE));
        }

        long timeToFinish = DateUtils.nowDiffInMs(challenge.getEndDate());

        if(timeToFinish < 0){
            tvTimer.setText("Challenge ended");
        } else {
            CountDownTimer timer = new MyCountDownTimer(timeToFinish, 1000);
            timer.start();
        }

        getSupportActionBar().setTitle(challenge.getTitle());

        goToParticipantsFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Challenge getChallenge(){
        return challenge;
    }

    public void goToParticipantsFragment() {
        ParticipantsFragment participantsFragment = new ParticipantsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, participantsFragment, participantsFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToEditBlocksFragment(Subscription item) {
        EditBlocksFragment editBlocksFragment = new EditBlocksFragment();
        Bundle args = new Bundle();
        args.putParcelable(EditBlocksFragment.EXTRA_SUBSCRIPTION, Parcels.wrap(item));
        editBlocksFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, editBlocksFragment, editBlocksFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToCompleteBlocksFragment(Subscription item) {
        CompleteBlocksFragment completeBlocksFragment = new CompleteBlocksFragment();
        Bundle args = new Bundle();
        args.putParcelable(CompleteBlocksFragment.EXTRA_SUBSCRIPTION, Parcels.wrap(item));
        completeBlocksFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, completeBlocksFragment, completeBlocksFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToWinnerFragment() {
        getSupportActionBar().hide();
        Utils.clearFragmentsBackStack(getSupportFragmentManager());
        WinnerFragment editBlocksFragment = new WinnerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, editBlocksFragment, editBlocksFragment.TAG);
        fragmentTransaction.commit();
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            tvTimer.setText("Challenge ended");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % (60);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % (60);

            String s = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);

            tvTimer.setText(s);
        }

    }

}

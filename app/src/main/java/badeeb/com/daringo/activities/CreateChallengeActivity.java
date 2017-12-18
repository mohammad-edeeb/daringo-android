package badeeb.com.daringo.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.fragments.ChallengeCreatedFragment;
import badeeb.com.daringo.fragments.CreateChallengeFragment;
import badeeb.com.daringo.fragments.InviteFriendsFragment;
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.utils.Utils;

public class CreateChallengeActivity extends CustomFontActivity {

    private FrameLayout flMainFrame;
    private List<FacebookFriend> invitedFriends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button_copy_2);

        flMainFrame = findViewById(R.id.flMainFrame);

        goToInviteFriendsFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<FacebookFriend> getInvitedFriends(){
        return invitedFriends;
    }

    public void goToInviteFriendsFragment() {
        InviteFriendsFragment inviteFriendsFragment = new InviteFriendsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, inviteFriendsFragment, inviteFriendsFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToCreateChallengeFragment(List<FacebookFriend> invitedFriends) {
        this.invitedFriends = invitedFriends;
        CreateChallengeFragment createChallengeFragment = new CreateChallengeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, createChallengeFragment, createChallengeFragment.TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToChallengeCreatedFragment() {
        getSupportActionBar().hide();
        Utils.clearFragmentsBackStack(getSupportFragmentManager());
        ChallengeCreatedFragment challengeCreatedFragment = new ChallengeCreatedFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, challengeCreatedFragment, challengeCreatedFragment.TAG);
        fragmentTransaction.commit();
    }
}

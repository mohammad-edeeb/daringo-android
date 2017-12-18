package badeeb.com.daringo.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedImageView;

import org.parceler.Parcels;

import java.util.Calendar;

import badeeb.com.daringo.R;
import badeeb.com.daringo.fragments.ActiveChallengesFragment;
import badeeb.com.daringo.fragments.PastChallengesFragment;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.responses.EmptyResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.AppSettings;
import badeeb.com.daringo.utils.CustomTypefaceSpan;
import badeeb.com.daringo.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends CustomFontActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_CURRENT_USER = "EXTRA_CURRENT_USER";

    private AppSettings settings;
    private User currentUser;
    public boolean deleteMode;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        settings = AppSettings.getInstance();


        if (getIntent().getParcelableExtra(EXTRA_CURRENT_USER) != null) {
            currentUser = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CURRENT_USER));
        } else {
            currentUser = settings.getUser();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateChallengeActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        applyFontToNavigationView(navigationView);

        setUserNameAndPhoto(navigationView.getHeaderView(0));

        navigationView.setCheckedItem(R.id.nav_active_challenges);
        goToActiveChallengesFragment();
    }

    public void onChallengeDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        if(deleteMode){
            UiUtils.hide(fab);
        } else {
            UiUtils.show(fab);
        }
        invalidateOptionsMenu();
    }


    private void setUserNameAndPhoto(View headerView) {
        RoundedImageView rivUserPhoto = headerView.findViewById(R.id.rivUserPhoto);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        Glide.with(this).load(currentUser.getImageUrl()).into(rivUserPhoto);
        String userName = "";
        if (!TextUtils.isEmpty(currentUser.getFirstName())) {
            userName += currentUser.getFirstName();
        }
        if (!TextUtils.isEmpty(currentUser.getLastName())) {
            userName += " " + currentUser.getLastName();
        }
        tvUserName.setText(userName);
    }

    private void applyFontToNavigationView(NavigationView navigationView) {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/opensans-regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(deleteMode){
            // clear to be deleted challenges
            ActiveChallengesFragment fragment = (ActiveChallengesFragment) getCurrentFragment();
            fragment.challengesRecyclerAdapter.revertDeleteMode();
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sigout) {
            callLogoutApi();
        } else if (id == R.id.nav_new_challenge) {
            Intent intent = new Intent(this, CreateChallengeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_invite) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_past_challenges) {
            if(!(getCurrentFragment() instanceof PastChallengesFragment)){
                goToPastChallengesFragment();
            }
        } else if (id == R.id.nav_active_challenges) {
            if(!(getCurrentFragment() instanceof ActiveChallengesFragment)) {
                goToActiveChallengesFragment();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.flMainFrame);
    }

    private void callLogoutApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        final Call<BaseResponse<EmptyResponse>> response = apiService.logout();
        response.enqueue(new Callback<BaseResponse<EmptyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<EmptyResponse>> call, Response<BaseResponse<EmptyResponse>> response) {
                if (response.code() == 200) {
                    settings = AppSettings.getInstance();
                    settings.clearUserInfo();
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<EmptyResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToActiveChallengesFragment() {
        UiUtils.show(fab);
        ActiveChallengesFragment activeChallengesFragment = new ActiveChallengesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, activeChallengesFragment, activeChallengesFragment.TAG);
        fragmentTransaction.commit();
    }

    public void goToPastChallengesFragment() {
        UiUtils.hide(fab);
        PastChallengesFragment pastChallengesFragment = new PastChallengesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMainFrame, pastChallengesFragment, pastChallengesFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_challenges, menu);
        if (!deleteMode) {
            menu.findItem(R.id.delete_button).setVisible(false);
        }
        return true;
    }

    private void generateConfirmDeleteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_white);

        Button bCancel = dialog.findViewById(R.id.bCancel);
        Button bConfirm = dialog.findViewById(R.id.bConfirm);

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActiveChallengesFragment fragment = (ActiveChallengesFragment) getCurrentFragment();
                fragment.callUnsubscribeChallengeApi();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button:
                generateConfirmDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

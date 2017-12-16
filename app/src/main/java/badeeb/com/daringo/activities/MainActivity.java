package badeeb.com.daringo.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedImageView;

import org.parceler.Parcels;

import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.adapters.ChallengesRecyclerAdapter;
import badeeb.com.daringo.adapters.OnRecyclerItemClick;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.UnsubscribeRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.responses.ChallengesListResponse;
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

    private RecyclerView rvChallenges;
    private ProgressBar pbLoading;
    private TextView tvNoChallenges;
    private TextView tvNumOfChallenges;
    private SwipeRefreshLayout srlChallengesList;

    private ChallengesRecyclerAdapter challengesRecyclerAdapter;
    private OnRecyclerItemClick<Challenge> onChallengeClickedListener;
    private AppSettings settings;
    private User currentUser;
    private boolean deleteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = AppSettings.getInstance();
        onChallengeClickedListener = createOnChallengeClickedListener();

        if (getIntent().getParcelableExtra(EXTRA_CURRENT_USER) != null) {
            currentUser = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CURRENT_USER));
        } else {
            currentUser = settings.getUser();
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        rvChallenges = findViewById(R.id.rvChallenges);
        challengesRecyclerAdapter = new ChallengesRecyclerAdapter(this);

        challengesRecyclerAdapter.setOnRecyclerItemClick(onChallengeClickedListener);

        rvChallenges.setAdapter(challengesRecyclerAdapter);
        rvChallenges.setLayoutManager(new LinearLayoutManager(this));

        pbLoading = findViewById(R.id.pbLoading);
        tvNoChallenges = findViewById(R.id.tvNoChallenges);
        tvNumOfChallenges = findViewById(R.id.tvNumOfChallenges);
        srlChallengesList = findViewById(R.id.srlChallengesList);
        srlChallengesList.setColorSchemeResources(R.color.colorAccent);


        srlChallengesList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callGetChallengesListApi();
            }
        });

        rvChallenges.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        setUserNameAndPhoto(navigationView.getHeaderView(0));
        callGetChallengesListApi();
    }

    private OnRecyclerItemClick<Challenge> createOnChallengeClickedListener() {
        return new OnRecyclerItemClick<Challenge>() {
            @Override
            public void OnRecyclerItemClick(View view, Challenge item, int position) {
                if (deleteMode) {
                    challengesRecyclerAdapter.highlightChallenge(item, position);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, InsideChallengeActivity.class);
                intent.putExtra(InsideChallengeActivity.EXTRA_CHALLENGE, Parcels.wrap(item));
                startActivity(intent);
            }
        };
    }

    private void callGetChallengesListApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UiUtils.hide(tvNoChallenges);

        if (!srlChallengesList.isRefreshing()) {
            UiUtils.show(pbLoading);
            UiUtils.hide(rvChallenges);
        }

        final Call<BaseResponse<ChallengesListResponse>> response = apiService.getChallengesList();
        response.enqueue(new Callback<BaseResponse<ChallengesListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ChallengesListResponse>> call, Response<BaseResponse<ChallengesListResponse>> response) {
                if (response.code() == 200) {
                    List<Challenge> challenges = response.body().getData().getChallenges();
                    tvNumOfChallenges.setText("(" + challenges.size() + ")");
                    if (!challenges.isEmpty()) {
                        challengesRecyclerAdapter.setItems(challenges);
                        challengesRecyclerAdapter.notifyDataSetChanged();
                        UiUtils.show(rvChallenges);
                        UiUtils.hide(tvNoChallenges);
                    } else {
                        UiUtils.hide(rvChallenges);
                        UiUtils.show(tvNoChallenges);
                    }

                    if (srlChallengesList.isRefreshing()) {
                        srlChallengesList.setRefreshing(false);
                    }
                    if (pbLoading.isShown()) {
                        UiUtils.hide(pbLoading);
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                    if (!srlChallengesList.isRefreshing()) {
                        UiUtils.hide(pbLoading);
                        UiUtils.show(rvChallenges);
                    } else {
                        srlChallengesList.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ChallengesListResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                if (!srlChallengesList.isRefreshing()) {
                    UiUtils.hide(pbLoading);
                    UiUtils.show(rvChallenges);
                } else {
                    srlChallengesList.setRefreshing(false);
                }
            }
        });
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
        } else if(challengesRecyclerAdapter.isDeleteMode()){
            // clear to be deleted challenges
            challengesRecyclerAdapter.revertDeleteMode();
        } else {
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

        } else if (id == R.id.nav_positions) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_challenges, menu);
        if (!deleteMode) {
            menu.findItem(R.id.delete_button).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button:
                callUnsubscribeChallengeApi();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void callUnsubscribeChallengeApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setChallenges(challengesRecyclerAdapter.getChallengesToBeDeleted());

        BaseRequest<UnsubscribeRequest> baseRequest = new BaseRequest<>(request);

        final Call<BaseResponse<EmptyResponse>> response = apiService.unsubscribe(baseRequest);
        response.enqueue(new Callback<BaseResponse<EmptyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<EmptyResponse>> call, Response<BaseResponse<EmptyResponse>> response) {
                if (response.code() == 200) {
                    challengesRecyclerAdapter.clearToBeDeleted();
                    callGetChallengesListApi();
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

    public void onChallengeDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        invalidateOptionsMenu();
    }
}

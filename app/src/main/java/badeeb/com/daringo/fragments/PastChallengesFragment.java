package badeeb.com.daringo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.adapters.OnRecyclerItemClick;
import badeeb.com.daringo.adapters.PastChallengesRecyclerAdapter;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.UnsubscribeRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.ChallengesListResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
import badeeb.com.daringo.models.responses.ErrorResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.UiUtils;
import badeeb.com.daringo.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PastChallengesFragment extends Fragment implements
        DeleteChallengeFragmentInterface {

    public static final String TAG = PastChallengesFragment.class.getSimpleName();

    private RecyclerView rvChallenges;
    private ProgressBar pbLoading;
    private TextView tvNoChallenges;
    private TextView tvNumOfChallenges;
    private SwipeRefreshLayout srlChallengesList;

    public PastChallengesRecyclerAdapter challengesRecyclerAdapter;
    private OnRecyclerItemClick<Challenge> onChallengeClickedListener;

    private MainActivity context;

    public PastChallengesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_past_challenges,
                container, false);

        onChallengeClickedListener = createOnChallengeClickedListener();

        rvChallenges = rootView.findViewById(R.id.rvChallenges);

        challengesRecyclerAdapter = new PastChallengesRecyclerAdapter(context);
        challengesRecyclerAdapter.setOnRecyclerItemClick(onChallengeClickedListener);

        rvChallenges.setAdapter(challengesRecyclerAdapter);
        rvChallenges.setLayoutManager(new LinearLayoutManager(context));

        pbLoading = rootView.findViewById(R.id.pbLoading);
        tvNoChallenges = rootView.findViewById(R.id.tvNoData);
        tvNumOfChallenges = rootView.findViewById(R.id.tvNumOfChallenges);
        srlChallengesList = rootView.findViewById(R.id.srlChallengesList);
        srlChallengesList.setColorSchemeResources(R.color.colorAccent);


        srlChallengesList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callGetPastChallengesListApi();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        callGetPastChallengesListApi();
    }

    private OnRecyclerItemClick<Challenge> createOnChallengeClickedListener() {
        return new OnRecyclerItemClick<Challenge>() {
            @Override
            public void OnRecyclerItemClick(View view, Challenge item, int position) {
                if (context.deleteMode) {
                    challengesRecyclerAdapter.highlightChallenge(item, position);
                }
            }
        };
    }

    public void callUnsubscribeChallengeApi() {
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
                    callGetPastChallengesListApi();
                } else if (response.code() == 422) {
                    ErrorResponse errorResponse = Utils.parseErrorResponse(response);
                    if(errorResponse != null &&
                            !TextUtils.isEmpty(errorResponse.getMeta().getMessage())){
                        Toast.makeText(context, errorResponse.getMeta().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    challengesRecyclerAdapter.clearToBeDeleted();
                    callGetPastChallengesListApi();
                }  else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<EmptyResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void revertDeleteMode() {
        challengesRecyclerAdapter.revertDeleteMode();
    }

    private void callGetPastChallengesListApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UiUtils.hide(tvNoChallenges);

        if (!srlChallengesList.isRefreshing()) {
            UiUtils.show(pbLoading);
            UiUtils.hide(rvChallenges);
        }

        final Call<BaseResponse<ChallengesListResponse>> response = apiService.getPastChallengesList();
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
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                if (!srlChallengesList.isRefreshing()) {
                    UiUtils.hide(pbLoading);
                    UiUtils.show(rvChallenges);
                } else {
                    srlChallengesList.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}

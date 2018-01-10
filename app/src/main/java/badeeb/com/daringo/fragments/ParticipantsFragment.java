package badeeb.com.daringo.fragments;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.InsideChallengeActivity;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.adapters.OnRecyclerItemClick;
import badeeb.com.daringo.adapters.SubscriptionsRecyclerAdapter;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.ErrorResponse;
import badeeb.com.daringo.models.responses.SubscriptionsListResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.AppSettings;
import badeeb.com.daringo.utils.UiUtils;
import badeeb.com.daringo.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantsFragment extends Fragment {

    public static final String TAG = ParticipantsFragment.class.getSimpleName();

    private RecyclerView rvParticipants;
    private ProgressBar pbLoading;

    private SubscriptionsRecyclerAdapter subscriptionsRecyclerAdapter;
    private OnRecyclerItemClick<Subscription> onParticipantClickedListener;
    private Subscription currentUserSubscription;


    private InsideChallengeActivity context;
    private AppSettings settings;


    public ParticipantsFragment() {
         settings = AppSettings.getInstance();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_participants, container, false);

        onParticipantClickedListener = createOnParticipantClickedListener();

        rvParticipants = rootView.findViewById(R.id.rvParticipants);
        pbLoading = rootView.findViewById(R.id.pbLoading);

        subscriptionsRecyclerAdapter = new SubscriptionsRecyclerAdapter(context, context.getChallenge());
        subscriptionsRecyclerAdapter.setOnRecyclerItemClick(onParticipantClickedListener);

        rvParticipants.setAdapter(subscriptionsRecyclerAdapter);
        rvParticipants.setLayoutManager(new LinearLayoutManager(context));

        callGetChallengeSubscriptionsApi();

        return rootView;
    }

    private void callGetChallengeSubscriptionsApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UiUtils.show(pbLoading);
        UiUtils.hide(rvParticipants);

        final Call<BaseResponse<SubscriptionsListResponse>> response = apiService.getChallengeSubscriptions(context.getChallenge().getId());
        response.enqueue(new Callback<BaseResponse<SubscriptionsListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SubscriptionsListResponse>> call, Response<BaseResponse<SubscriptionsListResponse>> response) {
                if (response.code() == 200) {
                    List<Subscription> subscriptions = response.body().getData().getSubscriptions();
                    setCurrentUserSubscription(subscriptions);
                    if(currentUserSubscription.isCanEdit()){
                        subscriptionsRecyclerAdapter.add(currentUserSubscription);
                    } else {
                        subscriptionsRecyclerAdapter.setItems(subscriptions);
                        subscriptionsRecyclerAdapter.notifyDataSetChanged();
                    }
                    UiUtils.show(rvParticipants);
                    if (pbLoading.isShown()) {
                        UiUtils.hide(pbLoading);
                    }
                } else if (response.code() == 422) {
                    ErrorResponse errorResponse = Utils.parseErrorResponse(response);
                    if(errorResponse != null &&
                            !TextUtils.isEmpty(errorResponse.getMeta().getMessage())){
                        Toast.makeText(context, errorResponse.getMeta().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                    UiUtils.show(rvParticipants);
                    if (pbLoading.isShown()) {
                        UiUtils.hide(pbLoading);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SubscriptionsListResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                UiUtils.show(rvParticipants);
                if (pbLoading.isShown()) {
                    UiUtils.hide(pbLoading);
                }
            }
        });
    }

    private void setCurrentUserSubscription(List<Subscription> subscriptions){
        for (Subscription s: subscriptions) {
            if(s.getUser().getId() == settings.getUserId()){
                currentUserSubscription = s;
                break;
            }
        }
    }

    private OnRecyclerItemClick<Subscription> createOnParticipantClickedListener() {
        return new OnRecyclerItemClick<Subscription>() {
            @Override
            public void OnRecyclerItemClick(View view, Subscription item, int position) {
                if(currentUserSubscription.isCanEdit()){
                    context.goToEditBlocksFragment(item);
                } else {
                    if(item.getUser().getId() != settings.getUserId()
                            && item.isCanEdit()){
                        Toast.makeText(context, "Waiting for user to add blocks", Toast.LENGTH_SHORT).show();
                    } else {
                        context.goToCompleteBlocksFragment(item);
                    }
                }
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (InsideChallengeActivity) context;
    }

}

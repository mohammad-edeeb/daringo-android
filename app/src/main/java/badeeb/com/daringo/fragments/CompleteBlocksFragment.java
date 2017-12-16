package badeeb.com.daringo.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.InsideChallengeActivity;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.adapters.CompleteBlocksRecyclerAdapter;
import badeeb.com.daringo.models.Block;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.UpdateSubscriptionRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.CompleteBlockResponse;
import badeeb.com.daringo.models.responses.SubscriptionDetailResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
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
public class CompleteBlocksFragment extends Fragment {

    public static final String TAG = CompleteBlocksFragment.class.getSimpleName();

    public static final String EXTRA_SUBSCRIPTION = "EXTRA_SUBSCRIPTION";

    private RoundedImageView rivPhoto;
    private TextView tvName;
    private TextView tvCompletedBlocks;
    private RecyclerView rvBlocks;
    private TextView tvPosition;
    private FloatingActionButton fabCompleteChallenge;

    private Subscription subscription;
    private CompleteBlocksRecyclerAdapter blocksAdapter;
    private InsideChallengeActivity context;
    private boolean currentUserSubscription;


    public CompleteBlocksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_complete_blocks, container, false);
        rivPhoto = rootView.findViewById(R.id.rivPhoto);
        tvName = rootView.findViewById(R.id.tvName);
        tvCompletedBlocks = rootView.findViewById(R.id.tvCompletedBlocks);
        rvBlocks = rootView.findViewById(R.id.rvBlocks);
        tvPosition = rootView.findViewById(R.id.tvPosition);
        fabCompleteChallenge = rootView.findViewById(R.id.fabCompleteChallenge);

        if(getArguments().getParcelable(EXTRA_SUBSCRIPTION) != null){
            subscription = Parcels.unwrap(getArguments().getParcelable(EXTRA_SUBSCRIPTION));
        }

        Glide.with(context).load(subscription.getUser().getImageUrl()).into(rivPhoto);
        tvName.setText(subscription.getUser().getName());
        setCompletedBlocks();
        tvPosition.setText(subscription.getCondition());

        AppSettings settings = AppSettings.getInstance();
        currentUserSubscription = subscription.getUser().getId() == settings.getUserId();

        blocksAdapter = new CompleteBlocksRecyclerAdapter(context, this,
                context.getChallenge(), subscription, currentUserSubscription);

        rvBlocks.setAdapter(blocksAdapter);
        rvBlocks.setLayoutManager(new LinearLayoutManager(context));

        fabCompleteChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCompleteChallengeApi();
            }
        });

        callGetSubscriptionDetailApi();

        return rootView;
    }

    private void setCompletedBlocks(){
        tvCompletedBlocks.setText(subscription.getNumOfCompletedBlocks()
                + "/" + context.getChallenge().getNumOfBlocks());
    }

    private void callUpdateSubscriptionApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        subscription.setCondition(tvPosition.getText().toString());
        subscription.setBlocks(blocksAdapter.getItems());
        request.setSubscription(subscription);

        final Call<BaseResponse<EmptyResponse>> response = apiService
                .updateSubscription(new BaseRequest<UpdateSubscriptionRequest>(request),
                        context.getChallenge().getId(), subscription.getId());

        response.enqueue(new Callback<BaseResponse<EmptyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<EmptyResponse>> call, Response<BaseResponse<EmptyResponse>> response) {
                if (response.code() == 200) {
                    context.goToParticipantsFragment();
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<EmptyResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callGetSubscriptionDetailApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        final Call<BaseResponse<SubscriptionDetailResponse>> response = apiService
                .getSubscriptionDetail(context.getChallenge().getId(), subscription.getId());

        response.enqueue(new Callback<BaseResponse<SubscriptionDetailResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SubscriptionDetailResponse>> call, Response<BaseResponse<SubscriptionDetailResponse>> response) {
                if (response.code() == 200) {
                    Subscription subscription = response.body().getData().getSubscription();
                    if(!currentUserSubscription){
                        List<Block> completedBlocks = new ArrayList<>();
                        for (Block block: subscription.getBlocks()) {
                            if(block.isCompleted()){
                                completedBlocks.add(block);
                            }
                        }
                        blocksAdapter.setItems(completedBlocks);
                    } else {
                        checkAllBlocksCompleted(subscription.getBlocks());
                        blocksAdapter.setItems(subscription.getBlocks());
                    }
                    blocksAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SubscriptionDetailResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAllBlocksCompleted(List<Block> blocks) {
        boolean completed = true;
        for (Block b: blocks) {
            if(!b.isCompleted()){
                completed = false;
                break;
            }
        }
        showFinishButton(completed);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (InsideChallengeActivity) context;
    }

    public void goToWinnerFragment() {
        context.goToWinnerFragment();
    }

    public void addCompletedBlock() {
        subscription.setNumOfCompletedBlocks(subscription.getNumOfCompletedBlocks() + 1);
        setCompletedBlocks();
    }

    public void removeCompletedBlock() {
        subscription.setNumOfCompletedBlocks(subscription.getNumOfCompletedBlocks() - 1);
        setCompletedBlocks();
    }

    private void callCompleteChallengeApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true).create(ApiInterface.class);

        final Call<BaseResponse<EmptyResponse>> response = apiService
                .completeChallenge(context.getChallenge().getId());

        response.enqueue(new Callback<BaseResponse<EmptyResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<EmptyResponse>> call, Response<BaseResponse<EmptyResponse>> response) {
                if (response.code() == 200) {
                    context.goToWinnerFragment();
                } else if (response.code() == 422){
                    UiUtils.showDialog(context, "Challenge already won by another participant",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<EmptyResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showFinishButton(boolean show) {
        if(show){
            UiUtils.show(fabCompleteChallenge);
        } else {
            UiUtils.hide(fabCompleteChallenge);
        }
    }
}

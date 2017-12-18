package badeeb.com.daringo.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.parceler.Parcels;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.InsideChallengeActivity;
import badeeb.com.daringo.adapters.EditBlocksRecyclerAdapter;
import badeeb.com.daringo.models.Subscription;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.UpdateSubscriptionRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.SubscriptionDetailResponse;
import badeeb.com.daringo.models.responses.EmptyResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBlocksFragment extends Fragment {
    public static final String TAG = EditBlocksFragment.class.getSimpleName();
    public static final String EXTRA_SUBSCRIPTION = "EXTRA_SUBSCRIPTION";

    private RoundedImageView rivPhoto;
    private TextView tvName;
    private TextView tvCompletedBlocks;
    private RecyclerView rvBlocks;
    private FloatingActionButton fabUpdate;
    private EditText etPosition;
    private ProgressBar pbLoading;

    private Subscription subscription;
    private EditBlocksRecyclerAdapter blocksAdapter;
    private InsideChallengeActivity context;


    public EditBlocksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_blocks, container, false);
        rivPhoto = rootView.findViewById(R.id.rivPhoto);
        tvName = rootView.findViewById(R.id.tvName);
        tvCompletedBlocks = rootView.findViewById(R.id.tvCompletedBlocks);
        rvBlocks = rootView.findViewById(R.id.rvBlocks);
        fabUpdate = rootView.findViewById(R.id.fabUpdate);
        etPosition = rootView.findViewById(R.id.etPosition);
        pbLoading = rootView.findViewById(R.id.pbLoading);

        if(getArguments().getParcelable(EXTRA_SUBSCRIPTION) != null){
            subscription = Parcels.unwrap(getArguments().getParcelable(EXTRA_SUBSCRIPTION));
        }

        Glide.with(context).load(subscription.getUser().getImageUrl()).into(rivPhoto);
        tvName.setText(subscription.getUser().getName());
        tvCompletedBlocks.setText(subscription.getNumOfCompletedBlocks() + "/" + context.getChallenge().getNumOfBlocks());
        etPosition.setText(subscription.getCondition());

        blocksAdapter = new EditBlocksRecyclerAdapter(context, this);

        rvBlocks.setAdapter(blocksAdapter);
        rvBlocks.setLayoutManager(new LinearLayoutManager(context));

        etPosition.addTextChangedListener(new MyTextWatcher());

        UiUtils.disable(fabUpdate);

        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUpdateSubscriptionApi();
            }
        });

        callGetSubscriptionDetailApi();

        return rootView;
    }

    private void callUpdateSubscriptionApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        subscription.setCondition(etPosition.getText().toString());
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

        UiUtils.show(pbLoading);
        UiUtils.hide(rvBlocks);

        final Call<BaseResponse<SubscriptionDetailResponse>> response = apiService
                .getSubscriptionDetail(context.getChallenge().getId(), subscription.getId());

        response.enqueue(new Callback<BaseResponse<SubscriptionDetailResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SubscriptionDetailResponse>> call, Response<BaseResponse<SubscriptionDetailResponse>> response) {
                if (response.code() == 200) {
                    Subscription subscription = response.body().getData().getSubscription();
                    blocksAdapter.setItems(subscription.getBlocks());
                    blocksAdapter.notifyDataSetChanged();
                    UiUtils.show(rvBlocks);
                    if (pbLoading.isShown()) {
                        UiUtils.hide(pbLoading);
                    }
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                    UiUtils.show(rvBlocks);
                    if (pbLoading.isShown()) {
                        UiUtils.hide(pbLoading);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SubscriptionDetailResponse>> call, Throwable t) {
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                UiUtils.show(rvBlocks);
                if (pbLoading.isShown()) {
                    UiUtils.hide(pbLoading);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (InsideChallengeActivity) context;
    }

    public void checkFieldsCompleted() {
        boolean valid = true;
        if(!blocksAdapter.allBlocksCompleted()){
            valid = false;
        }
        if(TextUtils.isEmpty(etPosition.getText())){
            valid = false;
        }
        if(valid){
            UiUtils.enable(fabUpdate);
        } else {
            UiUtils.disable(fabUpdate);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsCompleted();
        }
    }
}

package badeeb.com.daringo.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.CreateChallengeActivity;
import badeeb.com.daringo.activities.LoginActivity;
import badeeb.com.daringo.activities.MainActivity;
import badeeb.com.daringo.models.Challenge;
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.models.User;
import badeeb.com.daringo.models.requests.BaseRequest;
import badeeb.com.daringo.models.requests.CreateChallengeRequest;
import badeeb.com.daringo.models.requests.SocialLoginRequest;
import badeeb.com.daringo.models.responses.BaseResponse;
import badeeb.com.daringo.models.responses.CreateChallengeResponse;
import badeeb.com.daringo.models.responses.SocialLoginResponse;
import badeeb.com.daringo.network.ApiClient;
import badeeb.com.daringo.network.ApiInterface;
import badeeb.com.daringo.utils.DateUtils;
import badeeb.com.daringo.utils.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateChallengeFragment extends Fragment {

    public static final String TAG = CreateChallengeFragment.class.getSimpleName();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd kk:mm", Locale.US);

    private EditText etTitle;
    private EditText etDescription;
    private EditText etBlocks;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private FloatingActionButton fabCreate;
    private CreateChallengeActivity context;

    private Calendar startDateCal;
    private Calendar endDateCal;
    private Calendar minStartDateCal;


    public CreateChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_challenge, container, false);

        etTitle = rootView.findViewById(R.id.etTitle);
        etDescription = rootView.findViewById(R.id.etDescription);
        etBlocks = rootView.findViewById(R.id.etBlocks);
        tvStartDate = rootView.findViewById(R.id.tvStartDate);
        tvEndDate = rootView.findViewById(R.id.tvEndDate);
        fabCreate = rootView.findViewById(R.id.fabCreate);

        Calendar now = Calendar.getInstance();
        DateUtils.setToHourStart(now);

        startDateCal = DateUtils.addHours(now, 2);
        minStartDateCal = startDateCal;
        endDateCal = DateUtils.addDays(startDateCal, 1);

        setDateTextView(tvStartDate, startDateCal);
        setDateTextView(tvEndDate, endDateCal);

        UiUtils.disable(fabCreate);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDateTimePickerDialog(startDateCal, minStartDateCal, tvStartDate);
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateDateTimePickerDialog(endDateCal, startDateCal, tvEndDate);
            }
        });

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCreateChallengeApi();
            }
        });

        etTitle.addTextChangedListener(new MyTextWatcher());
        etDescription.addTextChangedListener(new MyTextWatcher());
        etBlocks.addTextChangedListener(new MyTextWatcher());

        return rootView;
    }

    private void callCreateChallengeApi() {
        ApiClient apiClient = new ApiClient();
        ApiInterface apiService = apiClient.getClient(true)
                .create(ApiInterface.class);

        CreateChallengeRequest request = createCreateChallengeRequest();

        final Call<BaseResponse<CreateChallengeResponse>> socialLoginResponse = apiService.createChallenge(new BaseRequest<CreateChallengeRequest>(request));
        socialLoginResponse.enqueue(new Callback<BaseResponse<CreateChallengeResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CreateChallengeResponse>> call, Response<BaseResponse<CreateChallengeResponse>> response) {
                UiUtils.hideKeyboardIfShown(context);
                if (response.code() == 200) {
                    context.goToChallengeCreatedFragment();
                } else {
                    Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CreateChallengeResponse>> call, Throwable t) {
                UiUtils.hideKeyboardIfShown(context);
                Toast.makeText(context, "Bad Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CreateChallengeRequest createCreateChallengeRequest(){
        Challenge newChallenge = new Challenge();
        newChallenge.setTitle(etTitle.getText().toString());
        newChallenge.setDescription(etDescription.getText().toString());
        int numOfBlocks = Integer.parseInt(etBlocks.getText().toString());
        newChallenge.setNumOfBlocks(numOfBlocks);

        newChallenge.setStartDate(startDateCal.getTime());
        newChallenge.setEndDate(endDateCal.getTime());

        List<User> participants = new ArrayList<>();

        for (FacebookFriend friend: context.getInvitedFriends()) {
            User participant = new User();
            participant.setSocialAccountId(friend.getId());
            participants.add(participant);
        }

        newChallenge.setParticipants(participants);

        CreateChallengeRequest request = new CreateChallengeRequest();
        request.setChallenge(newChallenge);
        return request;
    }

    private void checkFieldsCompleted(){
        boolean valid = true;
        if(TextUtils.isEmpty(etTitle.getText())){
            valid = false;
        }
        if(TextUtils.isEmpty(etBlocks.getText()) || etBlocks.getText().toString().equals("0")){
            valid = false;
        }
        if(valid){
            UiUtils.enable(fabCreate);
        } else {
            UiUtils.disable(fabCreate);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (CreateChallengeActivity) context;
    }

    private void setDateTextView(TextView tv, Calendar cal){
        tv.setText(dateFormat.format(cal.getTime()));
    }

    private void generateDateTimePickerDialog(final Calendar currentDateCal, final Calendar minDateCal,
                                              final TextView textView) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_datetime_picker_dialog);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        TextView positiveButton = (TextView) dialog.findViewById(R.id.positive_button);
        positiveButton.setText("Ok");

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if(minute != 0){
                    timePicker.setCurrentMinute(0);
                }
            }
        });

        // set minimum date
        datePicker.setMinDate(minDateCal.getTimeInMillis());

        // set current date
        int year = currentDateCal.get(Calendar.YEAR);
        int month = currentDateCal.get(Calendar.MONTH);
        int day = currentDateCal.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);

        // set current time
        timePicker.setCurrentHour(currentDateCal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(0);

        positiveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setDate();
                dialog.dismiss();
            }

            private void setDate() {
                currentDateCal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                currentDateCal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                currentDateCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                setDateTextView(textView, currentDateCal);
            }
        });


        dialog.show();
    }

    private class MyTextWatcher implements TextWatcher{

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

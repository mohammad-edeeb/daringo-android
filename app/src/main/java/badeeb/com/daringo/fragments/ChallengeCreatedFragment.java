package badeeb.com.daringo.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.CreateChallengeActivity;
import badeeb.com.daringo.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChallengeCreatedFragment extends Fragment {
    public static final String TAG = ChallengeCreatedFragment.class.getSimpleName();

    private Button bDone;

    private CreateChallengeActivity context;


    public ChallengeCreatedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (CreateChallengeActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_challenge_created, container, false);

        bDone = rootView.findViewById(R.id.bDone);

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return rootView;
    }

}

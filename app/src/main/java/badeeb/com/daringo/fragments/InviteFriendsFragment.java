package badeeb.com.daringo.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import badeeb.com.daringo.R;
import badeeb.com.daringo.activities.CreateChallengeActivity;
import badeeb.com.daringo.adapters.FriendsRecyclerAdapter;
import badeeb.com.daringo.adapters.OnRecyclerItemClick;
import badeeb.com.daringo.adapters.PhotosHorizontalRecyclerAdapter;
import badeeb.com.daringo.models.FacebookFriend;
import badeeb.com.daringo.utils.FacebookUtils;
import badeeb.com.daringo.utils.UiUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriendsFragment extends Fragment {

    public static final String TAG = InviteFriendsFragment.class.getSimpleName();

    private RecyclerView rvPhotos;
    private RecyclerView rvFriends;
    private TextView tvNoFriendsSelected;
    private FloatingActionButton fabNext;

    private FriendsRecyclerAdapter friendsAdapter;
    private PhotosHorizontalRecyclerAdapter photosAdapter;
    private CreateChallengeActivity context;

    public InviteFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friends, container, false);

        rvPhotos = rootView.findViewById(R.id.rvPhotos);
        rvFriends = rootView.findViewById(R.id.rvFriends);
        tvNoFriendsSelected = rootView.findViewById(R.id.tvNoFriendsSelected);
        fabNext = rootView.findViewById(R.id.fabNext);

        UiUtils.disable(fabNext);

        friendsAdapter = new FriendsRecyclerAdapter(context);
        photosAdapter = new PhotosHorizontalRecyclerAdapter(context);

        LinearLayoutManager photosLayoutManager = new LinearLayoutManager(context);
        photosLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager friendsLayoutManager = new LinearLayoutManager(context);
        friendsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvPhotos.setLayoutManager(photosLayoutManager);
        rvFriends.setLayoutManager(friendsLayoutManager);

        rvFriends.setAdapter(friendsAdapter);
        rvPhotos.setAdapter(photosAdapter);

        friendsAdapter.setOnRecyclerItemClick(createOnFriendCheckedClickListener());
        findFacebookFriends();

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToCreateChallengeFragment(photosAdapter.getItems());
            }
        });

        return rootView;
    }

    private OnRecyclerItemClick<FacebookFriend> createOnFriendCheckedClickListener() {
        return new OnRecyclerItemClick<FacebookFriend>() {
            @Override
            public void OnRecyclerItemClick(View view, FacebookFriend item, int position) {
                item.setChecked(!item.isChecked());
                friendsAdapter.refreshItem(position);
                if(item.isChecked()){
                    photosAdapter.add(item);
                } else {
                    photosAdapter.remove(item);
                }
                if(!photosAdapter.isEmpty()){
                    UiUtils.hide(tvNoFriendsSelected);
                    UiUtils.enable(fabNext);
                } else {
                    UiUtils.show(tvNoFriendsSelected);
                    UiUtils.disable(fabNext);
                }
            }
        };
    }

    private void findFacebookFriends(){
        GraphRequest request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray users, GraphResponse response) {
                ArrayList<FacebookFriend> friends = new ArrayList<>();

                if (users != null) {
                    for (int i = 0; i < users.length(); i++) {
                        try {
                            JSONObject currentUser = (JSONObject) users.get(i);
                            String id = currentUser.getString("id");
                            String name = currentUser.getString("name");
                            String imageUrl = FacebookUtils.getImageUrl(id);
                            FacebookFriend newFriend = new FacebookFriend(id, name, imageUrl);
                            friends.add(newFriend);

                            friendsAdapter.setItems(friends);
                            friendsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        request.executeAsync();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (CreateChallengeActivity) context;
    }

}

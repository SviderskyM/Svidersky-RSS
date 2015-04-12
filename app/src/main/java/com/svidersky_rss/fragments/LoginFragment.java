package com.svidersky_rss.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.svidersky_rss.R;

/**
 * Created by Eren on 15.03.2015.
 */
public class LoginFragment extends BaseFragment {
    private TextView userNameView;
    private ProfilePictureView profilepic;
    private Session activeSession = Session.getActiveSession();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        userNameView = (TextView) view.findViewById(R.id.user_name);
        profilepic = (ProfilePictureView) view.findViewById(R.id.user_photo);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        setContent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setContent();
    }

    private void setContent(){
        Request request = Request.newMeRequest(activeSession, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(final GraphUser user, Response response) {
                if (user != null) {
                    profilepic.setProfileId(user.getId());
                    userNameView.setText(user.getName());
                }
            }
        });
        Request.executeBatchAsync(request);
    }
}

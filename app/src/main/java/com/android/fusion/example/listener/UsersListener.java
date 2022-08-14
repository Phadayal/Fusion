package com.android.fusion.example.listener;

import com.android.fusion.example.Model.Friends;
import com.android.fusion.example.Model.User;


public interface UsersListener {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);

    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}

package com.android.fusion.example.Home;


import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.fusion.example.Adapter.UsersAdapter;
import com.android.fusion.example.Model.User;
import com.android.fusion.example.R;
import com.android.fusion.example.Utils.Constants;
import com.android.fusion.example.Utils.PreferenceManager;
import com.android.fusion.example.listener.UsersListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment implements UsersListener {

    private PreferenceManager preferenceManager;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TextView textErrorMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageConference;
    private View view;
//    private FirebaseUser user;
//    private FirebaseAuth mAuth;
    private  int REQUEST_CODE_BATTERY_OPTIMIZATIONS = 1;


    public CallsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_calls, container, false);

        preferenceManager = new PreferenceManager(getActivity());

        imageConference = view.findViewById(R.id.imageConference);

//        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();


//        user =mAuth.getCurrentUser();
        TextView textView = view.findViewById(R.id.textTitle);
        textView.setText(
                preferenceManager.getString(Constants.KEY_FIRST_NAME)
//                preferenceManager.getString(Constants.KEY_LAST_NAME)
        );


        view.findViewById(R.id.textCreateMeeting).setOnClickListener(view -> CreateMeeting());

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                sendFCMTokenToDatabase(task.getResult().getToken());
            }
        });

        RecyclerView usersRecyclerview = view.findViewById(R.id.recyclerViewUsers);
        textErrorMessage = view.findViewById(R.id.textErrorMessage);

        users = new ArrayList<>();

        usersAdapter = new UsersAdapter(users, this);
        usersRecyclerview.setAdapter(usersAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);

        getUsers();
//        checkForBatteryOptimizations();
        return view;
    }

    private void getUsers() {
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    swipeRefreshLayout.setRefreshing(false);
                    String myUsersId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        users.clear();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            if (myUsersId.equals(documentSnapshot.getId())) {
                                continue;
                            }

                            User user = new User();
                            user.firstName  = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
//                            user.lastName   = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email      = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token      = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);

//                            Log.e("Token1" , documentSnapshot.getString(Constants.KEY_FCM_TOKEN));
                        }

                        if (users.size() > 0) {
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            textErrorMessage.setText(String.format("%s", "No users available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textErrorMessage.setText(String.format("%s", "No users available"));
                        textErrorMessage.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCMTokenToDatabase(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
//                        user.getUid()
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Unable to send token: "+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void CreateMeeting() {

        Intent intent = new Intent(getActivity() , MeetingCallActivity.class);
        startActivity(intent);
    }

    @Override
    public void initiateVideoMeeting(User user) {
        Log.e("token" , user.token);
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(getActivity(), user.firstName+ " is not available for meeting", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);        }
    }

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(getActivity(), user.firstName+ " is not available for meeting", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }

    @Override
    public void onMultipleUsersAction(Boolean isMultipleUsersSelected) {
        if (isMultipleUsersSelected) {
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), OutgoingInvitationActivity.class);
                intent.putExtra("selectedUsers", new Gson().toJson(usersAdapter.getSelectedUsers()));
                intent.putExtra("type", "video");
                intent.putExtra("isMultiple", true);
                startActivity(intent);
            });
        } else {
            imageConference.setVisibility(View.GONE);
        }
    }

//    private void checkForBatteryOptimizations() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Warning");
//                builder.setMessage("Battery optimization is enabled. It can interrupt running background services.");
//                builder.setPositiveButton("Disable", (dialogInterface, i) -> {
//                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//                    startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZATIONS);
//                });
//                builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
//                builder.create().show();
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATIONS) {
//            checkForBatteryOptimizations();
        }
    }
}
package com.android.fusion.example.Home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.fusion.example.R;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MeetingCallActivity extends AppCompatActivity {
    String meetingName;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmeeting);

        editText = findViewById(R.id.room_name);
        button = findViewById(R.id.join);
//        meetingName = editText.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requestnewgroup();
            }
        });


    }

    private void Requestnewgroup() {

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(editText.getText().toString())
                    .setVideoMuted(true)
                    .setAudioOnly(true)
                    .build();


            JitsiMeetActivity.launch(MeetingCallActivity.this , options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

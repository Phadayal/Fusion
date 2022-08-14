package com.android.fusion.example.Home;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.fusion.example.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


//import org.checkerframework.checker.nullness.qual.*;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar m;
    private ImageView sendmessagebutton;
    @Nullable
    private EditText usermessageinput;
    private ScrollView scroll;
    private TextView displaytextmessage,groupchattext;
    private  FirebaseAuth mAuth;
    @Nullable private DatabaseReference UserRef,GroupNameRef,GroupMessageKeyRef;

    @Nullable private String currentGroupName,currentUserID, currentUserName, currentDate,currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_groupchatactivity);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        setTitle(currentGroupName);
//        Toast.makeText(GroupChatActivity.this,"",Toast.LENGTH_SHORT).show();

        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Group").child(currentGroupName);


        Initializefields();
        GetUserInfo();

        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();

                usermessageinput.setText("");
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        super.onStart();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SaveMessageInfoToDatabase()
    {
        String message = usermessageinput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();


        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this,"Please write message first...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForData = Calendar.getInstance();
            SimpleDateFormat currentDataFormat = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                currentDataFormat = new SimpleDateFormat("MMM dd ,YYYY");
            }
            currentDate = currentDataFormat.format(calForData.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentDate = currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);
            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String,Object> messageInfoMap =new HashMap<>();
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void GetUserInfo() {
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("search_name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Initializefields() {
//        m=(Toolbar)findViewById(R.id.group_chat_bar_layout);
//        setSupportActionBar(m);
//        getSupportActionBar().setTitle(currentGroupName);
        sendmessagebutton=(ImageView)findViewById(R.id.sendmessagebutton);
        usermessageinput=(EditText)findViewById(R.id.inputgroupmessage);
        displaytextmessage=(TextView)findViewById(R.id.groupchattextdisplay);
        groupchattext = findViewById(R.id.groupchattext);
        scroll=(ScrollView)findViewById(R.id.myscrollview);

    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();


            displaytextmessage.append(chatName + ":\n" +chatMessage +"\n" +chatDate + "\n\n\n");
//            groupchattext.setText(chatMessage);
            scroll.fullScroll(ScrollView.FOCUS_DOWN);


        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.colorWhite));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }


}
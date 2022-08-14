package com.android.fusion.example.Home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.android.fusion.example.R;
import com.android.fusion.example.ui.Tsbsaccessor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupFragment extends AppCompatActivity {

    private Toolbar m;
    private ViewPager myviewpager;
    private TabLayout t;
    private Tsbsaccessor tabs;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        mauth=FirebaseAuth.getInstance();
        currentuser=mauth.getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference();

        setTitle("Community Groups");




        myviewpager=(ViewPager)findViewById(R.id.main_tabs_pager);
        tabs=new Tsbsaccessor(getSupportFragmentManager());
        myviewpager.setAdapter(tabs);

        FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requestnewgroup();
            }
        });

//        t=(TabLayout)findViewById(R.id.main_tabs);
//        t.setupWithViewPager(myviewpager);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(currentuser==null)
//        {
//            loginactivity();
//        }
//        else
//        {
//            VerifyUserExistence();
//        }
//    }
//    private void VerifyUserExistence()
//    {
//        String UserId=mauth.getCurrentUser().getUid();
//        ref.child("Users").child("name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if((dataSnapshot.child("name").exists()))
//                {
//                    Toast.makeText(MainActivity.this,"Welcome!!!",Toast.LENGTH_LONG).show();     //User is previous user
//                }
////               else
////               {
////                   settingsactivity();//new user
////               }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.options_menu,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if(item.getItemId()==R.id.main_logout_friends)
//        {
//            mauth.signOut();
//            loginactivity();
//        }
//        if(item.getItemId()==R.id.main_setting_friends)
//        {
//            settingsactivity();
//
//        }
//        if(item.getItemId()==R.id.main_create_group)
//        {
//            Requestnewgroup();
//        }
//
//        return true;
//
//    }

    private void Requestnewgroup() {
        AlertDialog.Builder builder=new AlertDialog.Builder(CreateGroupFragment.this,R.style.AlertDialog);
        builder.setTitle("Enter Group name:-");
        final EditText groupnamefield=new EditText(CreateGroupFragment.this);
        builder.setView(groupnamefield);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupname=groupnamefield.getText().toString();
                if(TextUtils.isEmpty(groupname))
                {
                    Toast.makeText(CreateGroupFragment.this,"Please write group name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    createnewgroup(groupname);
                }
            }


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();


    }

    private void createnewgroup(final String groupname) {
        ref.child("Groups").child(groupname).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(CreateGroupFragment.this,groupname+"group is created successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
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

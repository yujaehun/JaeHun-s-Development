package edu.skku.map.personalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private ArrayList<String> db_username, db_password, db_fullname;
    EditText username, password;
    String user, pass;
    int check_id = -1, check_pw = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.userid);
        password = (EditText)findViewById(R.id.password);
        db_username = new ArrayList<String>();
        db_password = new ArrayList<String>();
        db_fullname = new ArrayList<String>();
        mPostReference = FirebaseDatabase.getInstance().getReference();

        if(getIntent().getExtras() != null){
            Intent signupIntent = getIntent();
            username.setText(signupIntent.getStringExtra("Username"));
        }

        mPostReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBasePost get = postSnapshot.getValue(FireBasePost.class);
                        String get_user, get_password, get_fullname;
                        get_user = get.username;
                        get_password = get.password;
                        get_fullname = get.fullname;
                        db_username.add(get_user);
                        db_password.add(get_password);
                        db_fullname.add(get_fullname);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                for(int i = 0; i < db_username.size(); i++){
                    if(db_username.get(i).equals(user)){
                        check_id = i;
                    }
                    if(db_password.get(i).equals(pass)){
                        check_pw = i;
                    }
                }
                if(check_id == -1){
                    Toast.makeText(MainActivity.this, "Wrong ID", Toast.LENGTH_SHORT).show();
                }
                else if(check_id != check_pw){
                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent loginIntent = new Intent(MainActivity.this, Main_Page.class);
                    loginIntent.putExtra("Username", user);
                    loginIntent.putExtra("Fullname", db_fullname.get(check_id));
                    startActivity(loginIntent);
                }
            }
        });

        TextView signup = (TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(MainActivity.this, SignUp.class);
                startActivity(signupIntent);
            }
        });


    }
}

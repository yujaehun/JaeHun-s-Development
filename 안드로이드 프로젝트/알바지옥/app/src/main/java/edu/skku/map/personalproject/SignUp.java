package edu.skku.map.personalproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private DatabaseReference mPostReference;
    String username = "", password = "", fullname = "", birthday = "", phone_number = "";
    EditText usernameET, passwordET, fullnameET, birthdayET, phone_numberET;
    private ArrayList<String > db_username;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameET = findViewById(R.id.signupUsername);
        passwordET = findViewById(R.id.signupPassword);
        fullnameET = findViewById(R.id.signupFullname);
        birthdayET = findViewById(R.id.signupBirthday);
        phone_numberET = findViewById(R.id.signupPhone_number);
        db_username = new ArrayList<>();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBasePost get = postSnapshot.getValue(FireBasePost.class);
                        String get_user;
                        get_user = get.username;
                        db_username.add(get_user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button login = findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
                fullname = fullnameET.getText().toString();
                birthday = birthdayET.getText().toString();
                phone_number = phone_numberET.getText().toString();

                if(username.length() * password.length() * fullname.length() * birthday.length() * phone_number.length() == 0){
                    Toast.makeText(SignUp.this, "Please fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (int i = 0; i < db_username.size(); i++) {
                        if (db_username.get(i).equals(username)) {
                            cnt = 1;
                        }
                    }
                    if(cnt == 1){
                        Toast.makeText(SignUp.this, "Please use another username", Toast.LENGTH_SHORT).show();
                        cnt = 0;
                    }
                    else{
                        postFirebaseDatabase(true);
                        Intent signupIntent = new Intent(SignUp.this, MainActivity.class);
                        signupIntent.putExtra("Username", username);
                        ClearET();
                        startActivity(signupIntent);
                    }
                }
            }
        });
    }

    private void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FireBasePost post = new FireBasePost(username, password, fullname, birthday, phone_number);
            postValues = post.tomap();
        }
        childUpdates.put("/User/"+username, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    private void ClearET() {
        usernameET.setText("");
        passwordET.setText("");
        fullnameET.setText("");
        birthdayET.setText("");
        phone_numberET.setText("");
        username = "";
        password = "";
        fullname = "";
        birthday = "";
        phone_number = "";
    }
}

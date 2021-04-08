package edu.skku.map.personalproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post_Store extends AppCompatActivity {

    private DatabaseReference mPostReference;
    String username = "", store_name = "", pay = "", job_type = "", phone_number = "", location = "", content = "";
    private ArrayList<String > db_store_name, db_location;
    EditText store_nameET, payET, job_typeET, phone_numberET, locationET, contentET;
    int cnt = 0;
    List<Address> list = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_store);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        db_store_name = new ArrayList<>();
        db_location = new ArrayList<>();

        store_nameET = findViewById(R.id.postStorename);
        payET = findViewById(R.id.postPay);
        job_typeET = findViewById(R.id.postJobtype);
        phone_numberET = findViewById(R.id.postPhone_number);
        locationET = findViewById(R.id.postLocation);
        contentET = findViewById(R.id.postContent);

        final Geocoder geocoder = new Geocoder(this);

        mPostReference.child("Store").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBaseStore get = postSnapshot.getValue(FireBaseStore.class);
                        db_store_name.add(get.store_name);
                        db_location.add(get.location);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent postIntent = getIntent();
        username = postIntent.getStringExtra("Username");
        Button post = findViewById(R.id.postButton);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_name = store_nameET.getText().toString();
                pay = payET.getText().toString();
                job_type = job_typeET.getText().toString();
                phone_number = phone_numberET.getText().toString();
                location = locationET.getText().toString();
                content = contentET.getText().toString();

                if(store_name.length() * pay.length() * job_type.length() * phone_number.length() * location.length() * content.length() == 0){
                    Toast.makeText(Post_Store.this, "Please fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else{
                    for(int i = 0; i < db_store_name.size(); i++){
                        if(db_store_name.get(i).equals(store_name) && db_location.get(i).equals(location)){
                            Toast.makeText(Post_Store.this, "This store already exists", Toast.LENGTH_SHORT).show();
                            cnt = 1;
                            break;
                        }
                    }
                    if(cnt == 0){

                        try{
                            list = geocoder.getFromLocationName(location, 10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(list != null){
                            if(list.size() == 0){
                                Toast.makeText(Post_Store.this, "There is no address", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                postFirebaseDatabase(true);
                                Intent signupIntent = new Intent(Post_Store.this, Main_Page.class);
                                signupIntent.putExtra("Username", username);
                                ClearET();
                                startActivity(signupIntent);
                            }
                        }
                        list = null;
                    }
                }
            }
        });

    }

    private void ClearET() {
        store_nameET.setText("");
        payET.setText("");
        job_typeET.setText("");
        phone_numberET.setText("");
        locationET.setText("");
        contentET.setText("");
        store_name = "";
        pay = "";
        job_type = "";
        phone_number = "";
        location = "";
        content = "";
    }

    private void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FireBaseStore post = new FireBaseStore(username, store_name, pay, job_type, phone_number, location, content, list.get(0).getLatitude(), list.get(0).getLongitude());
            postValues = post.tomap();
        }
        childUpdates.put("/Store/"+username+store_name, postValues);
        mPostReference.updateChildren(childUpdates);
    }
}

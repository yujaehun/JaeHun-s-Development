package edu.skku.map.personalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyResume extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
    }

    String username = "", get_store = "", get_fullname = "";
    int cnt = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myresume);

        TextView text_user = findViewById(R.id.resumeTitle);

        final Intent user = getIntent();
        text_user.setText(user.getStringExtra("Username"));
        username = user.getStringExtra("Username");

        final SR_listviewAdapter adapter = new SR_listviewAdapter();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();

        ListView listView = findViewById(R.id.resumelistView);
        listView.setAdapter(adapter);

        mPostReference.child("Resume").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FireBaseResume get = postSnapshot.getValue(FireBaseResume.class);
                        if (get.username.equals(username)) {
                            get_fullname = get.fullname;
                            get_store = get.store_name;
                            cnt = 1;
                            adapter.addItem(get_store, get_fullname);
                            adapter.notifyDataSetChanged();
                        }
                        get_fullname = "";
                        get_store = "";
                    }
                }
                if (cnt == 0) {
                    adapter.addItem("Not", " Exist");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(cnt != 0){
                    Intent myresume = new Intent(MyResume.this, Resume_detail.class);
                    myresume.putExtra("Username", username);
                    myresume.putExtra("Store", adapter.listviewItem_SR.get(position).getFullname());
                    startActivity(myresume);
                }
            }
        });
    }
}

package edu.skku.map.personalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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


public class MyStore extends AppCompatActivity {
    String username = "", get_store_name = "";
    int cnt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystore);

        final Mystore_listviewAdapter adapter = new Mystore_listviewAdapter();
        final Intent Main_page = getIntent();
        username = Main_page.getStringExtra("Username");
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        mPostReference.child("Store").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        FireBaseStore get = postSnapshot.getValue(FireBaseStore.class);
                        if(get.username.equals(username)){
                            get_store_name = get.store_name;
                            cnt = 1;
                            adapter.addItem(get_store_name);
                            adapter.notifyDataSetChanged();
                        }
                        get_store_name = "";
                    }
                }
                if(cnt == 0){
                    adapter.addItem("Not Exist");
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
                    Intent mystore = new Intent(MyStore.this, Submitted_Resume.class);
                    mystore.putExtra("Store", adapter.listviewItem_arr.get(position).getStore_name());
                    startActivity(mystore);
                }
            }
        });
    }
}

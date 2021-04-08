package edu.skku.map.personalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Submitted_Resume extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
    }

    String store = "", get_fullname = "", get_birth = "";
    int cnt = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_resume);

        TextView text_store = findViewById(R.id.SubmitresumeTitle);

        Intent mystore = getIntent();
        text_store.setText(mystore.getStringExtra("Store") +" Resume");
        store = mystore.getStringExtra("Store");

        final SR_listviewAdapter adapter = new SR_listviewAdapter();
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();

        ListView listView = findViewById(R.id.sb_resumelistView);
        listView.setAdapter(adapter);

        mPostReference.child("Resume").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        FireBaseResume get = postSnapshot.getValue(FireBaseResume.class);
                        if(get.store_name.equals(store)){
                            get_fullname = get.fullname;
                            get_birth = get.birthday;
                            cnt = 1;
                            adapter.addItem(get_fullname, get_birth);
                            adapter.notifyDataSetChanged();
                        }
                        get_fullname = "";
                        get_birth = "";
                    }
                }
                if(cnt == 0){
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
                    Intent submitted_resume = new Intent(Submitted_Resume.this, Resume.class);
                    submitted_resume.putExtra("Fullname", adapter.listviewItem_SR.get(position).getFullname());
                    submitted_resume.putExtra("Store", store);
                    startActivity(submitted_resume);
                }
            }
        });
    }
}

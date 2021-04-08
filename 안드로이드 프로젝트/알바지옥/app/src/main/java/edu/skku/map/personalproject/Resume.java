package edu.skku.map.personalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Resume extends AppCompatActivity {

    TextView fullnameET, birthET, phone_numberET, contentET;
    String fullname, store_name;
    ImageView imageView;
    private StorageReference mStoreageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        fullnameET = findViewById(R.id.resumename);
        birthET = findViewById(R.id.resumebirth);
        phone_numberET = findViewById(R.id.resumephone_number);
        contentET = findViewById(R.id.resumecontent);

        imageView = findViewById(R.id.imageView);
        mStoreageRef = FirebaseStorage.getInstance().getReference("resume_img");

        Intent SR_intent = getIntent();
        fullname = SR_intent.getStringExtra("Fullname");
        store_name = SR_intent.getStringExtra("Store");

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();

        mPostReference.child("Resume").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    FireBaseResume get = postSnapshot.getValue(FireBaseResume.class);
                    if(get.store_name.equals(store_name) && get.fullname.equals(fullname)){
                        if(!get.resume_img.equals(" ")){
                            StorageReference resume = mStoreageRef.child(get.resume_img);
                            final long ONE_MEGABYTE = 1024*1024*10;
                            resume.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imageView.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        fullnameET.setText(get.fullname);
                        birthET.setText(get.birthday);
                        phone_numberET.setText(get.phone_number);
                        contentET.setText(get.resume_content);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

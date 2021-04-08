package edu.skku.map.personalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Submit_Resume extends AppCompatActivity {

    private DatabaseReference mPostReference;
    private StorageReference mStoreageRef;

    String username = "", store_name = "", fullname = "", birthday = "", phone_number = "", content = "", resume_img;
    EditText fullnameET, birthdayET, phone_numberET, contentET;
    Button Submit;
    ImageButton imageButton;
    private static final int PICK_IMAGE = 777;
    Uri currentUri;
    boolean check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_resume);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        mStoreageRef = FirebaseStorage.getInstance().getReference("resume_img");

        Intent main_page = getIntent();

        username = main_page.getStringExtra("Username");
        store_name = main_page.getStringExtra("Store");

        fullnameET = findViewById(R.id.SubmitName);
        birthdayET = findViewById(R.id.SubmitBirth);
        phone_numberET = findViewById(R.id.SubmitPhone);
        contentET = findViewById(R.id.SubmitContent);

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        mPostReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FireBasePost get = postSnapshot.getValue(FireBasePost.class);
                    if(get.username.equals(username)){
                        fullnameET.setText(get.fullname);
                        birthdayET.setText(get.birthday);
                        phone_numberET.setText(get.phone_number);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Submit = findViewById(R.id.SubmitButton);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = fullnameET.getText().toString();
                birthday = birthdayET.getText().toString();
                phone_number = phone_numberET.getText().toString();
                content = contentET.getText().toString();
                if(fullname.length() * birthday.length() * content.length() * phone_number.length() == 0){
                    Toast.makeText(Submit_Resume.this, "Please fill all blanks", Toast.LENGTH_SHORT).show();
                }
                //error submit same user same store
                else{
                    if(check){
                        StorageReference riversRef = mStoreageRef.child(currentUri.getLastPathSegment());
                        UploadTask uploadTask = riversRef.putFile(currentUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                resume_img = currentUri.getLastPathSegment();
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                FireBaseResume post = new FireBaseResume(username, store_name, fullname, birthday, phone_number, content, resume_img);
                                postValues = post.tomap();

                                childUpdates.put("/Resume/"+username+store_name, postValues);
                                mPostReference.updateChildren(childUpdates);
                                Intent signupIntent = new Intent(Submit_Resume.this, Main_Page.class);
                                signupIntent.putExtra("Username", username);
                                ClearET();
                                startActivity(signupIntent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                    else{
                        resume_img = " ";
                        postFirebaseDatabase(true);
                        Intent signupIntent = new Intent(Submit_Resume.this, Main_Page.class);
                        signupIntent.putExtra("Username", username);
                        ClearET();
                        startActivity(signupIntent);
                    }
                }
            }
        });

    }

    private void ClearET() {
        fullnameET.setText("");
        birthdayET.setText("");
        phone_numberET.setText("");
        contentET.setText("");
        fullname = "";
        birthday = "";
        phone_number = "";
        content = "";
    }

    private void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FireBaseResume post = new FireBaseResume(username, store_name, fullname, birthday, phone_number, content, resume_img);
            postValues = post.tomap();
        }
        childUpdates.put("/Resume/"+username+store_name, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == -1) {
            ImageButton img = findViewById(R.id.imageButton);
            currentUri = data.getData();
            img.setImageURI(currentUri);
            check = true;
        }
    }
}

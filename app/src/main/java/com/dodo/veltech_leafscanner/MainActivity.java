package com.dodo.veltech_leafscanner;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ExtendedFloatingActionButton camerabtn,search;
    ImageView leaf,logout;
    LinearLayout srchbox;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    Uri img;
    StorageReference storageReference;
    String uri;
    private String user_id;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camerabtn = findViewById(R.id.camera);
        leaf = findViewById(R.id.leaf);
        search = findViewById(R.id.searchbtn);
        srchbox = findViewById(R.id.searchbox);
        logout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
        firebaseUser = mAuth.getCurrentUser();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this,login.class);
                startActivity(i);
                finish();
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.github.drjacky.imagepicker.ImagePicker.Companion.with(MainActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        leaf.setImageURI(uri);
        camerabtn.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srchbox.setVisibility(View.VISIBLE);

                UploadData(uri);
            }
        });
        //String img = uri.toString();

    }
    private void UploadData(Uri str1) {


        final StorageReference reference = storageReference.child("dataset2").child(System.currentTimeMillis() + "." + getFileExt(str1));
        UploadTask uploadTask = reference.putFile(str1);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(MainActivity.this, "uploading" , Toast.LENGTH_SHORT).show();

                    String timestamp = FieldValue.serverTimestamp().toString();
                    String uid = firebaseUser.getUid();
                    Map<String, String> data = new HashMap<>();


                    data.put("uid", uid);
                    data.put("email", mAuth.getCurrentUser().getEmail());
                    // data.put("timestamp", FieldValue.serverTimestamp());
                    data.put("imgurl", downloadUri.toString());

                    firebaseFirestore.collection("Plants").add(data)

                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    //progressBar.setVisibility(View.GONE);
                                    String user = "nursery";

                                    // getJson(downloadUri.toString());
                                    //getSuperHeroes(downloadUri.toString());

                                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();


                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "failure" + e, Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



}
package com.dodo.veltech_leafscanner;

import static com.google.android.gms.cast.framework.media.ImagePicker.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.cast.framework.media.TracksChooserDialogFragment;
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
    ExtendedFloatingActionButton camerabtn, search;
    ImageView leaf;
    LinearLayout srchbox;
    ProgressBar progress;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    String plantName, medicinalusesstr, partusestr, recipe;
    FirebaseStorage storage;
    Uri img;
    StorageReference storageReference;
    String uriimg;
    private String user_id;
    String phoneNumber;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = findViewById(R.id.progress);
        leaf = findViewById(R.id.leaf);
        search = findViewById(R.id.searchbtn);


        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();
        firebaseUser = mAuth.getCurrentUser();




        search.setOnClickListener(new View.OnClickListener() {
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


                UploadData(uri);

        //String img = uri.toString();

    }

    private void UploadData(Uri str1) {

        progress.setVisibility(View.VISIBLE);


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
                    uriimg = downloadUri.toString();
                    Toast.makeText(MainActivity.this, "uploading", Toast.LENGTH_SHORT).show();
                    String timestamp = FieldValue.serverTimestamp().toString();
                    String uid = firebaseUser.getUid();
                    Map<String, String> data = new HashMap<>();
                    //data.put("uid", uid);
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
                                    progress.setVisibility(View.GONE);
                                    voice_box();
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

    private void voice_box() {

        AlertDialog.Builder alertdialog1 = new AlertDialog.Builder(MainActivity.this);
        alertdialog1.setTitle("இது என்ன செடி/what plant is this?");
        EditText plantname = new EditText(MainActivity.this);
        plantname.setHint("தாவர பெயரை உள்ளிடவும்/Enter Plant Name");
        plantname.setGravity(Gravity.CENTER); //editbox in center

        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 20, 40, 20); //set margin

        LinearLayout lp = new LinearLayout(getApplicationContext());
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.addView(plantname, layoutParams);
        alertdialog1.setView(lp);
        alertdialog1.setPositiveButton("next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder alertdialog2 = new AlertDialog.Builder(MainActivity.this);
                        alertdialog2.setTitle("what are medicinal uses of this plant");
                        EditText medicinaluses = new EditText(MainActivity.this);
                        medicinaluses.setHint("what are medicinal uses of this plant");
                        medicinaluses.setGravity(Gravity.CENTER); //editbox in center

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(40, 20, 40, 20); //set margin

                        LinearLayout lp = new LinearLayout(getApplicationContext());
                        lp.setOrientation(LinearLayout.VERTICAL);

                        lp.addView(medicinaluses, layoutParams);
                        alertdialog2.setView(lp);
                        alertdialog2.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        AlertDialog.Builder alertdialog3 = new AlertDialog.Builder(MainActivity.this);
                                        alertdialog3.setTitle("which diseases can be cured?");
                                        final EditText partuse = new EditText(MainActivity.this);
                                        partuse.setHint("which diseases can be cured?");
                                        partuse.setGravity(Gravity.CENTER); //editbox in center

                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.setMargins(40, 20, 40, 20); //set margin

                                        LinearLayout lp = new LinearLayout(getApplicationContext());
                                        lp.setOrientation(LinearLayout.VERTICAL);

                                        lp.addView(partuse, layoutParams);


                                        alertdialog3.setView(lp);
                                        alertdialog3.setPositiveButton("YES",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        AlertDialog.Builder alertdialog4 = new AlertDialog.Builder(MainActivity.this);
                                                        alertdialog4.setTitle("how to use this  ");
                                                        final EditText recipebox = new EditText(MainActivity.this);
                                                        recipebox.setHint("இதை எப்படி பயன்படுத்துவது/how to use this");
                                                        recipebox.setGravity(Gravity.CENTER); //editbox in center

                                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                        layoutParams.setMargins(40, 20, 40, 20); //set margin

                                                        LinearLayout lp = new LinearLayout(getApplicationContext());
                                                        lp.setOrientation(LinearLayout.VERTICAL);

                                                        lp.addView(recipebox, layoutParams);


                                                        alertdialog4.setView(lp);
                                                        alertdialog4.setPositiveButton("submit",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                        recipe = recipebox.getText().toString();
                                                                        partusestr = partuse.getText().toString();
                                                                        medicinalusesstr = medicinaluses.getText().toString();
                                                                        plantName = plantname.getText().toString();

                                                                        UploadData(plantName, medicinalusesstr, partusestr, recipe);

                                                                    }
                                                                });
// Setting Negative "NO" Btn
                                                        alertdialog4.setNegativeButton("cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });

// Showing Alert Dialog
                                                        alertdialog4.show();
                                                    }
                                                });
// Setting Negative "NO" Btn
                                        alertdialog3.setNegativeButton("cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

// Showing Alert Dialog
                                        alertdialog3.show();
                                    }
                                });
// Setting Negative "NO" Btn
                        alertdialog2.setNegativeButton("cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.dismiss();
                                    }
                                });

// Showing Alert Dialog
                        alertdialog2.show();


                    }
                });

        alertdialog1.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertdialog1.show();
    }

    private void UploadData(String str1, String str2, String str3, String str4) {
        
        progress.setVisibility(View.VISIBLE);
        String uid = firebaseUser.getUid();
        Map<String, String> data = new HashMap<>();

        data.put("nameofplant", str1);
        data.put("medicinalusesstr", str2);
        data.put("partusestr", str2);
        data.put("recipe", str2);
        data.put("userid", user_id);

        data.put("uriimg", uriimg);


        firebaseFirestore.collection("tribal").add(data)

                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress.setVisibility(View.GONE);
                        String user = "nursery";
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);

                        startActivity(mainIntent);
                        finish();
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
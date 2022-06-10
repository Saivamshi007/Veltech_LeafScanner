package com.dodo.veltech_leafscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_screen extends AppCompatActivity {
    private int time=4000;
    private SharedPreferences mPreferences;
    boolean firstTime =false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView img = (ImageView)findViewById(R.id.flower);
        mAuth=FirebaseAuth.getInstance();
        Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation);
        img.startAnimation(aniRotate);
       // String android_id = Settings.Secure.getString(splash_screen.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user==null){
                    Intent intent = new Intent(splash_screen.this, login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent mainIntent= new Intent(splash_screen.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }




            }
        },time);



    }
}
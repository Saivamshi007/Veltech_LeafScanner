package com.dodo.veltech_leafscanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class user_role extends AppCompatActivity {

    RadioButton user,nursery;
    RadioGroup role;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    String str,str2,str3,str4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role);
        user = findViewById(R.id.user);
        nursery = findViewById(R.id.nursery);
        role = findViewById(R.id.role);
        extendedFloatingActionButton =findViewById(R.id.buttonnext);

        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i)
                {
                    case R.id.user:
                        str = "user";
                        str2 = "user1";
                        break;
                    case R.id.nursery:

                        str3="nursery";
                        str4="nursery2";

                        break;

                }
            }
        });
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isChecked()){
                    intent(str,str2);
                }else if(nursery.isChecked()){
                    Intent in3 = new Intent(getApplicationContext(), MainActivity.class);
                    in3.putExtra(str3, str4);
                    startActivity(in3);

                }
            }
        });


    }
    public void intent(String string,String string2){
        Intent in2 = new Intent(getApplicationContext(), MainActivity.class);
        in2.putExtra(string, string2);
        startActivity(in2);
        Toast.makeText(this, "fg"+string+string2, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        user_role.super.onBackPressed();
                    }
                }).create().show();
    }
}
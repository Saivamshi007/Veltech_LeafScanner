package com.dodo.veltech_leafscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Scanner extends AppCompatActivity {
    String url;
    ImageView holder;
    String bestmatch;
    TextView plantname,heading,commonnames;
    ProgressBar pbar;
    Button search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        holder = findViewById(R.id.plantimg);
        plantname = findViewById(R.id.plant_description);
        commonnames = findViewById(R.id.commonnames);
        heading = findViewById(R.id.plant___________name);
        pbar = findViewById(R.id.progress);
        search = findViewById(R.id.search);
        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(Scanner.this)
                .load(url)

                .into(holder);
        getSuperHeroes(url);


    }

    private void getSuperHeroes(String url1) {
        String url = "images="+url1;
        pbar.setVisibility(View.VISIBLE);


        // Call<ModelHome> data = YoutubeAPI.getVideo().getHomeVideo(url);
        Call<result> call = Plant_Api.getInstance().getMyApi().getPlants(url1,"leaf",false,false,"en","2b10QOGg4RJLO8JTXpTglM4p3");
        call.enqueue(new Callback<result>() {
            @Override
            public void onResponse(Call<result> call, Response<result> response) {


                String cn ="";
                List<String> cn2 = new ArrayList<>();
                result result1 = response.body();
                List<result.Species> sp2 = result1.results;
                for(result.Species sp : sp2){
                 cn2 = sp.commonNames;
                 cn = sp.scientificNameWithoutAuthor;
                    commonnames.setText(cn);
                }

                bestmatch = result1.getBestMatch();
                plantname.setText(bestmatch);

               // Toast.makeText(Scanner.this, (CharSequence) cn2, Toast.LENGTH_SHORT).show();



                heading.setVisibility(View.VISIBLE);

                plantname.setText(bestmatch);
                pbar.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Scanner.this,webview_leaf.class);
                        i.putExtra("leaf",bestmatch);
                        startActivity(i);

                    }
                });

                // listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, oneHeroes));
            }

            @Override
            public void onFailure(Call<result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "sorry not found please try again", Toast.LENGTH_LONG).show();
                Intent n = new Intent(Scanner.this,selfhelpgroups.class);
                startActivity(n);
                finish();
            }

        });
    }


}
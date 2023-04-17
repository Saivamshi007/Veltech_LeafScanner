package com.dodo.veltech_leafscanner;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Plant_Api {
    private static Plant_Api instance = null;
    private plant_interface myApi;

    private Plant_Api() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://my-api.plantnet.org/v2/identify/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(plant_interface.class);
    }

    public static synchronized Plant_Api getInstance() {
        if (instance == null) {
            instance = new Plant_Api();
        }
        return instance;
    }

    public plant_interface getMyApi() {
        return myApi;
    }
}

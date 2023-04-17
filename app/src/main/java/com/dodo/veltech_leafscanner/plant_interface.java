package com.dodo.veltech_leafscanner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface plant_interface {

    @GET("all")
    Call<result> getPlants(@Query("images") String images, @Query("organs") String organs , @Query("include-related-images") Boolean related, @Query("no-reject") Boolean rej, @Query("lang") String lang, @Query("api-key") String api);
}

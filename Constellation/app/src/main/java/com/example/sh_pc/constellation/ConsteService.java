package com.example.sh_pc.constellation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by sh_pc on 2016/6/21.
 */
public interface ConsteService {
    @GET("/bbtapi/constellation/constellation_query")
    Call<ConsteResult> getResult(
            @Header("apikey")String apikey ,
            @Query("consName")String constellation,
            @Query("type")String type
    );
}

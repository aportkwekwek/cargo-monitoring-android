package com.cargomonitoring;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


// Equivalent to service
public interface RetrofitInterface {

    @Headers({
      "Content-Type: application/json",
            "Accept: application/json"
    })

    @POST("/api/login")
    Call<User> executeLogin(@Body Login login);

    @POST("/api/get-task")
    Call<TaskModel> getCertainTask(@Body Tasks tasks);

    @GET("secretinfo")
    Call<ResponseBody> getSecret(@Header("Authorization") String token);

    @POST("/api/appuser/register")
    Call<Void> executeSign_up(@Body HashMap<String , String > map);



}

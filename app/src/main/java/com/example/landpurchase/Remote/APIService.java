package com.example.landpurchase.Remote;


import com.example.landpurchase.Models.DataMessage;
import com.example.landpurchase.Models.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService {

    @Headers (
        {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAGoTqvWI:APA91bGAR0PLSNt3LtoEi9PZEtkPR2FmqnfKWJnOcNf-Wb70EWnYCZdUkKLhNLxpMSpzJ_VJ_FXJi7_XEbyfdlMCU-Hv3BhaDerN_Tv7IvDXMApm-m6fUwOSWAi9Hk0zJWjqhjSKBlRD"
        }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}

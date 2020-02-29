package com.example.mpesa_stkpush.interfaces;


import com.example.mpesa_stkpush.api.response.STKPushResponse;


public interface STKListener {

    void onResponse(STKPushResponse stkPushResponse);

    void onError(Throwable throwable);
}

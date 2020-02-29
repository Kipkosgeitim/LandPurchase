package com.example.mpesa_stkpush.interfaces;


import com.example.mpesa_stkpush.api.response.STKPushResponse;

public interface STKQueryListener {

    void onResponse(STKPushResponse stkPushResponse);

    void onError(Throwable throwable);
}

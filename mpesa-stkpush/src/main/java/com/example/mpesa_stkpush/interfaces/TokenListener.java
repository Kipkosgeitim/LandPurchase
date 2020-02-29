package com.example.mpesa_stkpush.interfaces;

import com.example.mpesa_stkpush.model.Token;



public interface TokenListener {

    /**
     * method callback when token is generated successfully
     *
     * @param token - object from mpesa api response
     */
    void onTokenSuccess(Token token);

    /**
     * called when an error occurs
     *
     * @param throwable - an exception
     */
    void OnTokenError(Throwable throwable);
}

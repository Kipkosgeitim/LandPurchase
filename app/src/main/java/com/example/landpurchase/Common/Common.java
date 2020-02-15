package com.example.landpurchase.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;

import com.example.landpurchase.Models.User;
import com.example.landpurchase.Remote.APIService;
import com.example.landpurchase.Remote.GoogleRetrofitClient;
import com.example.landpurchase.Remote.IGoogleService;
import com.example.landpurchase.Remote.RetrofitClient;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static String currentKey;

    public static String topicName = "News";

    public static final String INTENT_Land_ID = "LandId";
    public static String countySelected ="";

    public static String PHONE_TEXT = "userPhone";

    private static final String  BASE_URL="https://fcm.googleapis.com/";

    private static final String  GOOGLE_API_URL="https://maps.googleapis.com/";
    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static IGoogleService getGoogleMapAPI()
    {
        return GoogleRetrofitClient.getGoolgeClient(GOOGLE_API_URL).create(IGoogleService.class);
    }

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On my way";
        else if (code.equals("2"))
            return "Shipping";
        else
            return "Shipped";
    }


    public static final String  DELETE ="Delete";
    public static final String USER_KEY ="User";
    public static final String  PWD_KEY ="Password";
    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager !=null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for(int i=0; i<info.length;i++)
                {
                    if(info[i].getState() ==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;

    }

    public static BigDecimal formatCurrency(String amount, Locale locale) throws ParseException, java.text.ParseException {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        if (format instanceof DecimalFormat)
            ((DecimalFormat)format).setParseBigDecimal(true);

        return (BigDecimal)format.parse(amount.replace("[^\\d.,]",""));
    }


}

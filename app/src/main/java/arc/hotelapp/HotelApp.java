package arc.hotelapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class HotelApp extends Application {

    public static String user_email,password,name,phone,hotelId,hotelName;
    public static Set<String> room_Types;

    public static final boolean themeDark = true;

    private static final String TAG = "Horizon";
    public static final String EMAIL = "email";

    public static final String months[] = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Log.d(TAG, "attachBaseContext(): ");
        getDataFromSharedPreference();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate(): ");
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void StoreUserDataLocallyInSharedPreferences(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",user_email);
        editor.putString("password",password);
        editor.putString("name",name);
        editor.putString("hotelId",hotelId);
        editor.putString("phone",phone);

        Set<String> toStore = new HashSet<>();
        if(room_Types != null)
            toStore.addAll(room_Types);
        editor.putStringSet("room_Types",toStore);
        editor.apply();
        Log.d(TAG, "StoreUserDataLocallyInSharedPreferences: email = "+user_email+ " pasword = "+password);
    }

    public  void getDataFromSharedPreference()
    {
        SharedPreferences editor = this.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        user_email = editor.getString("email",null);
        password = editor.getString("password",null);
        name = editor.getString("name",null);
        hotelId = editor.getString("hotelId",null);
        phone = editor.getString("phone",null);
        room_Types = editor.getStringSet("room_Types",new HashSet<>());
        Log.d(TAG, "StoreUserDataLocallyInSharedPreferences: email = "+user_email+ " pasword = "+password);
    }

    public static String reFormatDateString(String str)
    {
        Log.d(TAG, "reFormatDateString: datew = "+str);
        int index = str.indexOf("T");
        StringBuilder stringBuilder = new StringBuilder();

        str = str.substring(0,index);

        String[] arr_str = str.split("-");

        return arr_str[2]+"-"+arr_str[1]+"-"+arr_str[0];
    }

}

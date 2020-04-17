package arc.hotelapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;

import com.google.android.material.snackbar.Snackbar;

public abstract class Internet_BroadcastReceiver extends BroadcastReceiver {
    Snackbar snackbar;
    private static final String TAG = "Internet_BroadcastRecei";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );

            if (noConnectivity) {
                snackbar = showSnackBar();
                snackbar.getView().setBackgroundColor(Color.parseColor("#e60000"));
                snackbar.show();
            } else if(snackbar != null && snackbar.isShown()) {
                    snackbar.dismiss();
            }
        }
    }

    public  abstract Snackbar showSnackBar();

}

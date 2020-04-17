package arc.hotelapp.Auth;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import arc.hotelapp.HotelApp;
import arc.hotelapp.Internet_BroadcastReceiver;
import arc.hotelapp.MainActivity;
import arc.hotelapp.R;
import arc.hotelapp.Auth.SignUp.SignUpActivity;

import static arc.hotelapp.HotelApp.hotelId;
import static arc.hotelapp.HotelApp.isNetworkConnected;
import static arc.hotelapp.HotelApp.room_Types;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

/**The facebook login was seen from youtube channel TV/AC studios*/

public class LoginActivity extends AppCompatActivity {

    Internet_BroadcastReceiver internetBroadcastReceiver = new Internet_BroadcastReceiver() {
        @Override
        public Snackbar showSnackBar() {
            return Snackbar.make(coordinatorLayout,"You are not connected",Snackbar.LENGTH_INDEFINITE);
        }
    };

    //..Only Views
    private CoordinatorLayout coordinatorLayout;


    String TAG = "LoginActivity";
    EditText email, password;
    ImageButton signInBtn;
    ProgressBar progressBar;
    TextView appname;
    LinearLayout input_form;

    //..miscelleneous
    private AwesomeValidation mAwesomeValidation;

    //animations
    Animation bottomToUp;
    Animation appear;

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appname = findViewById(R.id.appname);
        input_form = findViewById(R.id.input_form);
        coordinatorLayout = findViewById(R.id.coord);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signInBtn = findViewById(R.id.sign_in_button);
        progressBar = findViewById(R.id.progress);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        addValidationForEditText(this);

        //..animation for app name
        bottomToUp = AnimationUtils.loadAnimation(this,R.anim.bottom_to_up);
        input_form.setAnimation(bottomToUp);

        appear = AnimationUtils.loadAnimation(this,R.anim.appearence_animation);
        appname.setAnimation(appear);

        password.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if(!isNetworkConnected(v.getContext()))
                {
                    Toast.makeText(v.getContext(),"OOPS!!! No Internet..",Toast.LENGTH_LONG).show();
                    return false;
                }
                hideKeyboard(LoginActivity.this);
                signInWithEmail();
                handled = true;
            }
            return handled;
        });

        signInBtn.setOnClickListener(v -> {
            Log.d(TAG, "onClick: sign in button clicked ");
            if(!isNetworkConnected(v.getContext()))
            {
                Toast.makeText(v.getContext(),"OOPS!!! No Internet..",Toast.LENGTH_LONG).show();
                return;
            }

            hideKeyboard(LoginActivity.this);
            signInWithEmail();
        });
    }

    private void addValidationForEditText(Activity activity)
    {
        mAwesomeValidation.addValidation(activity, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        String regexPassword = "(?=.[a-z])(?=.[A-Z])(?=.+[0-9])(?=.[\\d])(?=.[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}t";
        mAwesomeValidation.addValidation(activity,R.id.password,".+",R.string.err_password);
    }

    public void openSignUpActivity(View v)
    {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //..register the internet broadcastreceiver
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetBroadcastReceiver); //..unregister the BroadcastReceiver since there is no use of it as the app is not in front
    }

    public void signInWithEmail()
    {
        boolean valid = mAwesomeValidation.validate();
        if(valid)
        {
            hidesignInbutton();

            //..sets colour of progress bar to app theme colour
            int colorCodeDark = ContextCompat.getColor(getApplicationContext(),R.color.app_theme_color);
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));

            AndroidNetworking.post(getResources().getString(R.string.serverurl)+"login/admin")
                    .addBodyParameter("email", email.getText().toString())
                    .addBodyParameter("password",password.getText().toString())
                    .setTag("signin")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("name").equals("null"))
                                {
                                    Toast.makeText(LoginActivity.this,"Incorrect email or password",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                getUserData(response);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d(TAG, "onError: "+anError.getErrorBody());
                            showSignInbutton();
                        }
                    });

        }
    }

    //..reappears signIn button and hides progressbar
    public void showSignInbutton()
    {
        signInBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    //..disappears signIn button and shows progressbar
    public void hidesignInbutton()
    {
        signInBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    //..common method to extract data from user JSONObject from server, all types of login methods access this method
    public void getUserData(JSONObject user) throws Exception
    {
        Log.d(TAG, "getUserData() "+user.toString());
        HotelApp.user_email = user.getString("email");
        HotelApp.name = user.getString("name");
        HotelApp.phone = user.getString("phone");
        HotelApp.hotelId = user.getString("hotelID");
        HotelApp.password = password.getText().toString();
        AndroidNetworking.get(getResources().getString(R.string.serverurl)+"getHotel/"+hotelId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try{
                            HotelApp.hotelName = response.getString("hotelName");
                            room_Types = new HashSet<>();
                            JSONArray jsonArray = response.getJSONArray("roomTypes");

                            for(int i = 0;i < jsonArray.length();i++)
                            {
                                String temp = jsonArray.getString(i);
                                room_Types.add(temp);
                            }
                            Log.d(TAG, "onResponse: roomstyeps = "+room_Types);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        HotelApp.StoreUserDataLocallyInSharedPreferences(LoginActivity.this);
                        goToMainActivity();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });


    }


    public void goToMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
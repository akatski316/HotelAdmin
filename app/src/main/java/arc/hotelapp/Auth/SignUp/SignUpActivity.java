package arc.hotelapp.Auth.SignUp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.File;

import arc.hotelapp.HotelApp;
import arc.hotelapp.R;

import static arc.hotelapp.HotelApp.isNetworkConnected;


public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button signup;

    LottieAnimationView lottieAnimationView;
    ViewPager viewPager;
    FormAdapter formAdapter;

    Animation animation;
    private static final String TAG = "SignUpActivity";

    String email_text,password_text,gender,name;
    File profile_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        lottieAnimationView = findViewById(R.id.cartoon);
        viewPager = findViewById(R.id.frame_layout);

        formAdapter = new FormAdapter(getSupportFragmentManager());
        viewPager.setAdapter(formAdapter);

        animation = AnimationUtils.loadAnimation(this,R.anim.appearence_animation);
        lottieAnimationView.setAnimation(animation);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cartoonClick(View v)
    {
        lottieAnimationView.playAnimation();
    }

    public void getVerificationEmail(View view)
    {
        ((EmailFragment)formAdapter.getItem(0)).submit(view);
        view.setVisibility(View.GONE);
        if(!isNetworkConnected(this))
        {
            Toast.makeText(this,"OOPS!!! No Internet..",Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "sendVerificationEmail: email = "+email_text+" password = "+password_text+" name = "+name+" gender = "+gender+" profile_photo = "+profile_photo);

        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"/signup")
                .addBodyParameter("email",email_text)
                .addBodyParameter("password",password_text)
                .addBodyParameter("name",name)
                .setTag("signup")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String response){

                        Log.d(TAG, "onResponse: "+response);
                        Toast.makeText(SignUpActivity.this,response,Toast.LENGTH_SHORT).show();
                        finish();
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ANError anError){
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

}

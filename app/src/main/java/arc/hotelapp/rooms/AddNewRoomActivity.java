package arc.hotelapp.rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import arc.hotelapp.R;

import static arc.hotelapp.HotelApp.hotelId;
import static arc.hotelapp.HotelApp.user_email;

public class AddNewRoomActivity extends AppCompatActivity {

    RadioButton selected_radio_button;
    RadioGroup radioGroup;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    EditText roomNumber,capacity;

    private static final String TAG = "AddNewRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_room);
        radioGroup = findViewById(R.id.roomType);
        floatingActionButton = findViewById(R.id.floating_action_button);
        progressBar = findViewById(R.id.progress);
        roomNumber = findViewById(R.id.roomNumber);
        capacity = findViewById(R.id.capacity);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFloatingActionButton();
                checkRadioButton();
                String roomType = selected_radio_button.getText().toString().toUpperCase();

                AndroidNetworking.post(getResources().getString(R.string.serverurl)+"admin/addRoom")
                        .addBodyParameter("email",user_email)
                        .addBodyParameter("hotelId",hotelId)
                        .addBodyParameter("roomNumber",roomNumber.getText().toString())
                        .addBodyParameter("capacity",capacity.getText().toString())
                        .addBodyParameter("roomType",roomType)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "onResponse: "+response);
                                finish();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d(TAG, "onError: "+anError.getErrorBody());
                                showFloatingActionButton();
                            }
                        });

            }
        });
    }

    void checkRadioButton()
    {
        int id = radioGroup.getCheckedRadioButtonId();
        selected_radio_button = findViewById(id);


    }

    void hideFloatingActionButton()
    {
        floatingActionButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void showFloatingActionButton()
    {
        floatingActionButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}

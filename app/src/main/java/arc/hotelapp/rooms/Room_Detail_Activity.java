package arc.hotelapp.rooms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import arc.hotelapp.BookingDetailActivity;
import arc.hotelapp.HotelApp;
import arc.hotelapp.R;

public class Room_Detail_Activity extends AppCompatActivity {

    TextView room_no_textview,booking_status_textview,room_type_textview,capacity_textview;
    Button booking_details_btn;
    String roomNumber,roomType,capacity,bookingId,roomId;
    boolean isBooked;
    FloatingActionButton floatingActionButton;
    Intent intent;

    private static final String TAG = "Events_Detail_Activity";
    public static final String ROOMNUMBER = "ROOMNUMBER",ROOMTYPE = "ROOMTYPE",CAPACITY = "CAPACITY",ISBOOKED = "ISBOOKED",BOOKINGID = "BOOKINGID"
            ,ROOMID = "ROOMID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }

        Log.d(TAG, "onCreate: "+TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        room_no_textview = findViewById(R.id.room_no);
        room_type_textview = findViewById(R.id.roomType);
        capacity_textview = findViewById(R.id.capacity);
        floatingActionButton = findViewById(R.id.editRoom);

        intent = getIntent();
        roomNumber = intent.getStringExtra(ROOMNUMBER);
        roomType = intent.getStringExtra(ROOMTYPE);
        capacity=intent.getStringExtra(CAPACITY);
        bookingId = intent.getStringExtra(BOOKINGID);
        isBooked = intent.getBooleanExtra(ISBOOKED,false);
        roomId = intent.getStringExtra(ROOMID);

        room_no_textview.setText(roomNumber);
        room_type_textview.setText(roomType);

        capacity_textview.setText(capacity);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRoomDialog editRoomDialog = new EditRoomDialog(roomId,Room_Detail_Activity.this);
                editRoomDialog.show(getSupportFragmentManager(),"edit_dialog");
            }
        });

    }


}

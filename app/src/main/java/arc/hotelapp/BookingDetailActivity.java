package arc.hotelapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import arc.hotelapp.rooms.rooms_fragment.Room;


public class BookingDetailActivity extends AppCompatActivity {

    public static final String HOTELNAME = "HOTELNAME"
            ,ROOMID = "ROOMID"
            ,ROOMTYPE = "ROOMTYPE"
            ,CHECKIN = "CHECKIN"
            ,CHECKOUT = "CHECKOUT"
            ,GUESTS = "GUESTS"
            ,VISITOREMAIL = "VISITOREMAIL"
            ,BOOKINGID = "BOOKINGID";
    private static final String TAG = "BookingDetailActivity";
    Intent intent;
    String roomId,roomNumber,hotelName,checkin,checkout,roomType,guests,visitor_email,visitor_name,bookingId;
    TextView textview_name, textview_hotelname,textview_email,textview_roomType,textview_no_of_guest,textview_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        textview_email = findViewById(R.id.email);
        textview_hotelname = findViewById(R.id.hotelName);
        textview_name = findViewById(R.id.name);
        textview_no_of_guest = findViewById(R.id.no_of_guest);
        textview_roomType = findViewById(R.id.roomType);
        textview_date = findViewById(R.id.date);


        intent = getIntent();
        bookingId = intent.getStringExtra(BOOKINGID);
        hotelName = intent.getStringExtra(HOTELNAME);
        guests = intent.getStringExtra(GUESTS);
        visitor_email = intent.getStringExtra(VISITOREMAIL);
        roomType = intent.getStringExtra(ROOMTYPE);
        checkin = intent.getStringExtra(CHECKIN);
        checkout = intent.getStringExtra(CHECKOUT);
        roomId = intent.getStringExtra(ROOMID);
        getBooking(bookingId);

    }

    void getBooking(String bookingId)
    {
        Log.d(TAG, "getRoom: "+bookingId);
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"getRoom")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("roomId",roomId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Room room = Room.parse(response);
                            roomNumber = room.getRoomNumber();
                            showOnUI();
                            getUser();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    void getUser()
    {
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"admin/getVisitor")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("visitorEmail",visitor_email)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            visitor_name = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        textview_name.setText(visitor_name);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showOnUI() {
        Log.d(TAG, "showOnUI: name = " +HotelApp.name+" "+hotelName);


        textview_email.setText(visitor_email);
        textview_roomType.setText(roomType);
        textview_no_of_guest.setText(guests);
        textview_hotelname.setText(hotelName);
        textview_name.setText(visitor_name);
        textview_date.setText("from "+checkin+" to "+checkout);

    }
}

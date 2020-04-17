package arc.hotelapp.bookings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import arc.hotelapp.BookingDetailActivity;
import arc.hotelapp.HotelApp;
import arc.hotelapp.R;


public class BookingsFragment extends Fragment implements BookingsAdapter.OnEventItemClicked{
    View view;
    RecyclerView recyclerView;
    List<Booking> my_bookings;
    BookingsAdapter bookingsAdapter;
    FilterDialog filterDialog;

    public static String person_name,checkIn,checkOut;
    private static final String TAG = "BookingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        filterDialog = new FilterDialog(getContext(),this);
        filterDialog.setCancelable(false);
        filterDialog.show(getFragmentManager(),"flter_dialog");
        view = inflater.inflate(R.layout.fragment_bookings, container, false);
        recyclerView = view.findViewById(R.id.bookings);

        return view;
    }

    void updateRecyclerView()
    {
        Log.d(TAG, "updateRecyclerView: "+my_bookings.size());
        bookingsAdapter = new BookingsAdapter(this,my_bookings);
        recyclerView.setAdapter(bookingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
    }

    void extractBookings(JSONArray jsonArray)
    {
        my_bookings = new ArrayList<>();
        for(int i = 0;i < jsonArray.length();i++)
        {
            try {
                my_bookings.add(Booking.parse(jsonArray.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(int position, List<Booking> list) {
        Booking toPass = list.get(position);
        Intent intent = new Intent(getContext(), BookingDetailActivity.class);
        intent.putExtra(BookingDetailActivity.HOTELNAME,toPass.getHotelName());
        intent.putExtra(BookingDetailActivity.CHECKIN,toPass.getCheckIn());
        intent.putExtra(BookingDetailActivity.CHECKOUT,toPass.getCheckOut());
        intent.putExtra(BookingDetailActivity.GUESTS,toPass.getNo_of_guests());
        intent.putExtra(BookingDetailActivity.VISITOREMAIL,toPass.getVisitorEmail());
        intent.putExtra(BookingDetailActivity.BOOKINGID,toPass.getBookingId());
        intent.putExtra(BookingDetailActivity.ROOMID,toPass.getRoomId());
        Log.d(TAG, "onItemClick: "+toPass.getVisitorEmail());
        getContext().startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bookings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void filterByDate() {
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"/admin/getBookings")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("checkIn",checkIn)
                .addBodyParameter("checkOut",checkOut)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        extractBookings(response);
                        updateRecyclerView();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    public void filterByName() {
        Log.d(TAG, "filterByName: "+person_name);
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"admin/getBookingForVisitor")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("name",person_name)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        extractBookings(response);
                        updateRecyclerView();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }


}

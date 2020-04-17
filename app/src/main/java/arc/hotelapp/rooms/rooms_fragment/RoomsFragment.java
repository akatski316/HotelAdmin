package arc.hotelapp.rooms.rooms_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import arc.hotelapp.R;
import arc.hotelapp.rooms.AddNewRoomActivity;

import static arc.hotelapp.HotelApp.hotelId;
import static arc.hotelapp.HotelApp.hotelName;
import static arc.hotelapp.HotelApp.user_email;


public class RoomsFragment extends Fragment {

    View view;
    TextView textView;

    public static boolean updatingUi = false;

    public static List<Room> roomList = new ArrayList<>();

    private static final String TAG = "RoomsFragment";

    FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    GridView gridView;
    RoomCardViewAdapter roomCardViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView(): ");
        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        setHasOptionsMenu(true);
        floatingActionButton = view.findViewById(R.id.addRoom);
        swipeRefreshLayout = view.findViewById(R.id.swipetorefresh);
        textView = view.findViewById(R.id.hotelName);
        updatingUi = false;
        gridView = view.findViewById(R.id.grid);
        startLoadingRoomData();

        textView.setText(hotelName);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoadingRoomData();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRoom();
            }
        });

        return view;
    }

    private void startLoadingRoomData()
    {
        swipeRefreshLayout.setRefreshing(true);
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"getRooms")
                .addBodyParameter("email", user_email)
                .addBodyParameter("hotelId",hotelId)
                .setTag("signin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        swipeRefreshLayout.setRefreshing(false);
                        roomList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Room room = Room.parse(response.getJSONObject(i));
                                roomList.add(room);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        roomCardViewAdapter = new RoomCardViewAdapter(roomList,getContext(),getActivity());
                        gridView.setAdapter(roomCardViewAdapter);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }


    private void addNewRoom()
    {
        Intent intent = new Intent(getContext(), AddNewRoomActivity.class);
        startActivity(intent);
    }

}

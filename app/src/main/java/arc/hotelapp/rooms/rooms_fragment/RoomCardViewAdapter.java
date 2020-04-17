package arc.hotelapp.rooms.rooms_fragment;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import arc.hotelapp.R;
import arc.hotelapp.rooms.Room_Detail_Activity;

public class RoomCardViewAdapter extends BaseAdapter {

    List<Room> list_of_rooms;
    Context context;
    FragmentActivity activity;

    private static final String TAG = "HorizontalEventsAdapter";

    public RoomCardViewAdapter(List<Room> list_of_rooms, Context context, FragmentActivity activity) {
        this.list_of_rooms = list_of_rooms;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list_of_rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return list_of_rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = list_of_rooms.get(position);
        View view;
        if(convertView == null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_room_card, parent, false);

            TextView textView1 = view.findViewById(R.id.roomNumber);
            textView1.setText(room.getRoomNumber());

            TextView textView2 = view.findViewById(R.id.roomType);
            textView2.setText(room.getRoomType());

            TextView textView3 = view.findViewById(R.id.roomCapacity);
            textView3.setText(room.getCapacity());
        }
        else
            view = convertView;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"position is "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Room_Detail_Activity.class);
                Room temp_room = list_of_rooms.get(position);

                intent.putExtra(Room_Detail_Activity.ROOMNUMBER,temp_room.getRoomNumber());
                intent.putExtra(Room_Detail_Activity.ROOMTYPE,temp_room.getRoomType());
                intent.putExtra(Room_Detail_Activity.CAPACITY,temp_room.getCapacity());
                intent.putExtra(Room_Detail_Activity.ISBOOKED,temp_room.isBooked());
                intent.putExtra(Room_Detail_Activity.BOOKINGID,temp_room.getBookingId());
                intent.putExtra(Room_Detail_Activity.ROOMID,temp_room.getRoomId());
                context.startActivity(intent);
            }
        });
        return view;
    }
}

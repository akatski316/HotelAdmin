package arc.hotelapp.bookings;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import arc.hotelapp.R;


public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.MyBookingsViewHolder> {
    private static final String TAG = "InActiverSubjectsAdapte";
    OnEventItemClicked onEventItemClicked;
    List<Booking> list;

    public BookingsAdapter(OnEventItemClicked onEventItemClicked, List<Booking> list) {
        this.onEventItemClicked = onEventItemClicked;
        this.list = list;
    }

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.mybookings_each_item,viewGroup,false);
        return new MyBookingsViewHolder(view,onEventItemClicked,list);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder(): "+list.get(position).getCheckIn()+" size of list = "+list.size());
        holder.checkIn.setText(list.get(position).getCheckIn());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyBookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView checkIn;
        OnEventItemClicked onEventItemClicked;
        List<Booking> list;

        public MyBookingsViewHolder(@NonNull View itemView, OnEventItemClicked onEventItemClicked, List<Booking> list) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.checkIn = itemView.findViewById(R.id.inactive_subject);
            this.onEventItemClicked = onEventItemClicked;
            this.list = list;
        }

        @Override
        public void onClick(View v) {
            onEventItemClicked.onItemClick(getAdapterPosition(),list);
        }
    }

    public interface OnEventItemClicked
    {
        void onItemClick(int position, List<Booking> list);
    }
}

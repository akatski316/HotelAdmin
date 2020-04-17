package arc.hotelapp.bookings;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;

import arc.hotelapp.BookingDetailActivity;
import arc.hotelapp.HotelApp;
import arc.hotelapp.R;

import static arc.hotelapp.bookings.BookingsFragment.checkIn;

public class FilterDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "RoomTypesDialog";
    View view;
    RadioButton enter_name,date;
    DatePickerDialog datePickerDialog;
    Context context;
    BookingsFragment fragment;

    public FilterDialog(Context context,BookingsFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_filter_dialog,null);
        enter_name = view.findViewById(R.id.enter_name);
        date = view.findViewById(R.id.date);

        builder.setView(view)
                .setTitle("Choose your room type(s)").setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(enter_name.isChecked()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Enter name");
                    final EditText input = new EditText(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    builder.setView(input);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BookingsFragment.person_name = input.getText().toString().trim();

                            BookingsFragment.checkIn = BookingsFragment.checkOut = null;
                            fragment.filterByName();
//                            filterByName();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });
                    builder.show();
//                    ((BookingsFragment)view.getParentFragment()).filterByName();


                }
                else {
                    datePicker();
//                    filterByDate();
                }


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }

    void datePicker()
    {
        BookingsFragment.checkIn = BookingsFragment.checkOut = BookingsFragment.person_name = null;
        datePickerDialog = new DatePickerDialog(context,this
                , Calendar.getInstance().get(Calendar.YEAR)
                ,Calendar.getInstance().get(Calendar.MONTH)
                ,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(checkIn == null)
        {
            checkIn = year+"-"+month+"-"+dayOfMonth;
            datePickerDialog = new DatePickerDialog(context,this
                    , Calendar.getInstance().get(Calendar.YEAR)
                    ,Calendar.getInstance().get(Calendar.MONTH)
                    ,Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }
        else
        {
            BookingsFragment.checkOut = year+"-"+month+"-"+dayOfMonth;
            fragment.filterByDate();
        }
    }


}

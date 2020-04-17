package arc.hotelapp.rooms;

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
import android.widget.RadioGroup;
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
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;

import arc.hotelapp.BookingDetailActivity;
import arc.hotelapp.HotelApp;
import arc.hotelapp.R;

import static arc.hotelapp.bookings.BookingsFragment.checkIn;

public class EditRoomDialog extends DialogFragment {
    private static final String TAG = "EditRoomDialog";
    View view;
    String roomId;
    EditText editText;
    String roomType;
    RadioButton standard,deluxe,luxury;
    RadioGroup radioGroup;

    public EditRoomDialog(String roomId, Context context) {
        this.roomId = roomId;
        this.context = context;
    }

    Context context;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_room,null);;
        editText = view.findViewById(R.id.capacity);
        standard = view.findViewById(R.id.standard);
        deluxe = view.findViewById(R.id.deluxe);
        luxury = view.findViewById(R.id.luxury);
        radioGroup = view.findViewById(R.id.radiogroup);

        if(!HotelApp.room_Types.contains(standard.getText().toString().toUpperCase()))
            standard.setVisibility(View.GONE);

        if(!HotelApp.room_Types.contains(deluxe.getText().toString().toUpperCase()))
            deluxe.setVisibility(View.GONE);

        if(!HotelApp.room_Types.contains(luxury.getText().toString().toUpperCase()))
            luxury.setVisibility(View.GONE);

        builder.setView(view)
                .setTitle("edit your room")
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int radiobtn = radioGroup.getCheckedRadioButtonId();
                roomType = ((RadioButton)view.findViewById(radiobtn)).getText().toString().toUpperCase();
                changeRoom();
            }
        });

        return builder.create();
    }


    void changeRoom()
    {
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"/admin/editRoom")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("roomId",roomId)
                .addBodyParameter("newCapacity",editText.getText().toString().trim())
                .addBodyParameter("newRoomType",roomType)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }


}

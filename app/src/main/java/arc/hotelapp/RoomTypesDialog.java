package arc.hotelapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class RoomTypesDialog extends DialogFragment {
    static final String STANDARD = "STANDARD";
    static final String DELUXE = "DELUXE";
    static final String LUXURY = "LUXURY";
    private static final String TAG = "RoomTypesDialog";
    View view;
    CheckBox standard,deluxe,luxury;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_addroom_type,null);
        standard = view.findViewById(R.id.standard);
        deluxe = view.findViewById(R.id.deluxe);
        luxury = view.findViewById(R.id.luxury);
        Log.d(TAG, "onCreateDialog: "+HotelApp.room_Types);
        if(HotelApp.room_Types.contains(STANDARD))
            standard.setChecked(true);
        if(HotelApp.room_Types.contains(DELUXE))
            deluxe.setChecked(true);
        if(HotelApp.room_Types.contains(LUXURY))
            luxury.setChecked(true);

        builder.setView(view)
        .setTitle("Choose your room type(s)").setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(standard.isChecked())
                    HotelApp.room_Types.add(STANDARD);

                if(deluxe.isChecked())
                    HotelApp.room_Types.add(DELUXE);

                if(luxury.isChecked())
                    HotelApp.room_Types.add(LUXURY);

                HotelApp.StoreUserDataLocallyInSharedPreferences(getContext());
                for(String type : HotelApp.room_Types)
                    saveRoomTypesOnServer(type);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    void saveRoomTypesOnServer(String type)
    {
        AndroidNetworking.post(getResources().getString(R.string.serverurl)+"/admin/addRoomType")
                .addBodyParameter("email", HotelApp.user_email)
                .addBodyParameter("roomType",type)
                .setTag("signin")
                .setPriority(Priority.HIGH)
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

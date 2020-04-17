package arc.hotelapp;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import arc.hotelapp.bookings.BookingsFragment;
import arc.hotelapp.rooms.rooms_fragment.RoomsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    Toolbar toolbar, searchtoolbar;
    Menu search_menu;
    MenuItem item_search;
    TextView name;
    CoordinatorLayout coordinatorLayout;
    EditText txtSearch;


    private String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;

    //..an object that instantiates a userdefined BroadcastReceiver for making sure internet is connected
    Internet_BroadcastReceiver internetBroadcastReceiver = new Internet_BroadcastReceiver() {
        @Override
        public Snackbar showSnackBar() {
            return Snackbar.make(coordinatorLayout,"You are not connected",Snackbar.LENGTH_INDEFINITE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator);

        mOnNavigationItemSelectedListener= item -> {
            switch (item.getItemId()) {

                case R.id.navigation_events:
                    setFragment(new RoomsFragment());
                    name = toolbar.findViewById(R.id.fragment_name);
                    name.setText("Rooms");
                    return true;

                case R.id.navigation_notice:
                    RoomTypesDialog roomTypesDialog = new RoomTypesDialog();
                    roomTypesDialog.show(getSupportFragmentManager(),"room dialog");
                    return true;

                case R.id.navigation_booking:
                    setFragment(new BookingsFragment());
                    name = toolbar.findViewById(R.id.fragment_name);
                    name.setText("Bookings");
                    return true;

            }

            return false;
        };

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(savedInstanceState == null) {
            setFragment(new RoomsFragment());
            navigation.setSelectedItemId(R.id.navigation_events);
            name = toolbar.findViewById(R.id.fragment_name);
            name.setText("Rooms");
        }
    }

    public void setFragment(Fragment frag)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,frag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //..ConnectivityManager.CONNECTIVITY_ACTION is deprecated in Api level 28
        //..whenever activity is in fron we regiter it to the system to broadcast network connection
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetBroadcastReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //..whenever activity is not in front it unregisters our broadcastreceiver
        unregisterReceiver(internetBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.action_filter:
                Toast.makeText(this, "refreshing notice", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

}

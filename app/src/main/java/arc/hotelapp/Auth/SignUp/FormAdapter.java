package arc.hotelapp.Auth.SignUp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FormAdapter extends FragmentPagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Button button;
    Animation animation;
    Fragment[] fragments;

    public FormAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new Fragment[1];
        fragments[0] = new EmailFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 1;
    }

}

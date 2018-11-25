package uk.ac.cam.cl.part1b.groupproject.echo.energybudget;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.io.IOException;

import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.TipDatabase;
import uk.ac.cam.cl.part1b.groupproject.echo.energybudget.fragments.tips.TipsFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private HomeFragment homeFragment;
    private DevicesFragment devicesFragment;
    private UsageFragment usageFragment;
    private BadgesFragment badgesFragment;
    private TipsFragment tipsFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(homeFragment);
                    return true;
                case R.id.navigation_devices:
                    if (devicesFragment == null){
                        devicesFragment = new DevicesFragment();
                        devicesFragment.viewCount = 0;
                    }
                    devicesFragment.viewCount++;
                    changeFragment(devicesFragment);
                    return true;
                case R.id.navigation_usage:
                    if (usageFragment == null){
                        usageFragment = new UsageFragment();
                    }
                    changeFragment(usageFragment);
                    return true;
                case R.id.navigation_badges:
                    if (badgesFragment == null){
                        badgesFragment = new BadgesFragment();
                    }
                    changeFragment(badgesFragment);
                    return true;
                case R.id.navigation_tips:
                    if (tipsFragment == null){
                        tipsFragment = new TipsFragment();
                    }
                    changeFragment(tipsFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        UserData.getUser(user -> {
            Log.d(TAG, "User data fetched.");
        });
    }

    public void changeFragment (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}

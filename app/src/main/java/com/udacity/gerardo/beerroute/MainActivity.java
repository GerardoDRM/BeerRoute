package com.udacity.gerardo.beerroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.udacity.gerardo.beerroute.utils.BeerApplication;

public class MainActivity extends AppCompatActivity {
    private final String MAIN_FRAGMENT = "MAIN_FRAGMENT";
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new MainActivityFragment(), MAIN_FRAGMENT)
                    .commit();
        }

        // Obtain the shared Tracker instance.
        BeerApplication application = (BeerApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}

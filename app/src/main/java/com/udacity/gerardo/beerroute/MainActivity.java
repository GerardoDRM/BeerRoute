package com.udacity.gerardo.beerroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.utils.BeerApplication;
import com.udacity.gerardo.beerroute.utils.GeneralConst;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.CallbackBeer{
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

    @Override
    public void onItemSelected(Beer beer) {
        // create the shared transition animation
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(GeneralConst.BEER_KEY, beer);
        intent.putExtras(mBundle);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
//                    Bundle bundle = ActivityOptions
//                            .makeSceneTransitionAnimation(mActivity, holder.mThumbnail, holder.mThumbnail.getTransitionName()).toBundle();

            startActivity(intent);
        } else {
            startActivity(intent);
        }
    }
}

package com.udacity.gerardo.beerroute;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private final String DETAILS_FRAGMENT = "BEER_FRAGMENT_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_details, new DetailActivityFragment(), DETAILS_FRAGMENT)
                    .commit();
        }

    }

}

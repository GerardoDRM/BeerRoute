package com.udacity.gerardo.beerroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gerardo.beerroute.utils.GeneralConst;

public class RouteActivity extends AppCompatActivity {

    private final String ROUTE_FRAGMENT = "ROUTE_FRAGMENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            if (intent.getExtras().containsKey(GeneralConst.BEER_KEY)) {
                RouteActivityFragment route = new RouteActivityFragment();
                Bundle args = new Bundle();
                args.putString(GeneralConst.BEER_KEY, intent.getStringExtra(GeneralConst.BEER_KEY));
                route.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_route, route, ROUTE_FRAGMENT)
                        .commit();

            }

        }

    }

}

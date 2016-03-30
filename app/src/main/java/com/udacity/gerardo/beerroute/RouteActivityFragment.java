package com.udacity.gerardo.beerroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.gerardo.beerroute.API.BeerApi;
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.utils.CircleTransform;
import com.udacity.gerardo.beerroute.utils.GeneralConst;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class RouteActivityFragment extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.thumbnail)
    ImageView mPhoto;
    @Bind(R.id.beer_origen)
    TextView mOrigen;
    @Bind(R.id.beer_title) TextView mName;
    @Bind(R.id.beers_continue)
    LinearLayout mToContinueCard;
    @Bind(R.id.beers_end)
    LinearLayout mToEndCard;

    private String BASE_URL;
    private Retrofit retrofit;
    private BeerApi apiService;

    public RouteActivityFragment() {
        this.BASE_URL = GeneralConst.BASE_URL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(BeerApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route, container, false);
        ButterKnife.bind(this, rootView);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(GeneralConst.BEER_KEY)) {
                String beerId = arguments.getString(GeneralConst.BEER_KEY);
                showBeerRoute(beerId);
            }
        }



        return rootView;
    }

    private void showBeerRoute(String beerId) {
        Call<Beer> call = apiService.getRoute(beerId);
        call.enqueue(new Callback<Beer>() {

            @Override
            public void onResponse(Response<Beer> response, Retrofit retrofit) {
                int statusCode = response.code();
                Beer beerResult = response.body();
                mName.setText(beerResult.getName());
                mOrigen.setText(beerResult.getOrigen());
                Picasso.with(getContext()).load(beerResult.getImage()).transform(new CircleTransform()).into(mPhoto);

                // Create Beer sequence
                // To continue
                for (Beer beerC : beerResult.getmToContinue()) {
                    insertBeerOnCard(beerC, mToContinueCard);
                }
                // To End
                for (Beer beerE : beerResult.getmToEnd()) {
                    insertBeerOnCard(beerE, mToEndCard);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.d("Error", t.getMessage());
            }
        });

    }

    private void insertBeerOnCard(Beer beer, LinearLayout cardView) {
        View beersView = getActivity().getLayoutInflater().inflate(R.layout.item_beer, null);
        TextView title = (TextView) beersView.findViewById(R.id.beer_title);
        title.setText(beer.getName());
        TextView origen = (TextView) beersView.findViewById(R.id.beer_origen);
        origen.setText(beer.getOrigen());
        ImageView photo = (ImageView) beersView.findViewById(R.id.thumbnail);
        Picasso.with(getContext()).load(beer.getImage()).transform(new CircleTransform()).into(photo);

        cardView.addView(beersView);
    }

}

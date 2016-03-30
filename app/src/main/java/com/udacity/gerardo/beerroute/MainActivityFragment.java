package com.udacity.gerardo.beerroute;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gerardo.beerroute.API.BeerApi;
import com.udacity.gerardo.beerroute.adapter.BeerCursorAdapter;
import com.udacity.gerardo.beerroute.adapter.CatalogAdapter;
import com.udacity.gerardo.beerroute.data.BeerProvider;
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.model.BeerResult;
import com.udacity.gerardo.beerroute.utils.GeneralConst;

import java.util.ArrayList;

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
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.adView) AdView mAdView;
    private CatalogAdapter adapter;
    private String BASE_URL;
    private Retrofit retrofit;
    private BeerApi apiService;
    private static final int CURSOR_LOADER_ID = 0;
    private BeerCursorAdapter mCursorAdapter;


    public interface CallbackBeer {
        public void onItemSelected(Beer beer);
        public void onFavoriteSelected(Uri uri);
    }

    public MainActivityFragment() {
        this.BASE_URL = GeneralConst.BASE_URL;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);

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
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mCursorAdapter = new BeerCursorAdapter(getActivity(), null);
        adapter = new CatalogAdapter(getActivity(), new ArrayList<Beer>());
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 2));
        mRecyclerView.setAdapter(adapter);
        getBeers(0);

    }

    public void getFavorites() {
        mRecyclerView.setAdapter(mCursorAdapter);
    }

    public void getBeers(int elements) {
        Call<BeerResult> call = apiService.getBeers(elements);
        call.enqueue(new Callback<BeerResult>() {

            @Override
            public void onResponse(Response<BeerResult> response, Retrofit retrofit) {
                int statusCode = response.code();
                BeerResult beerResult = response.body();
                adapter.refill(beerResult.getResults());
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.d("Error", t.getMessage());
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Adding action to menu items
        switch (item.getItemId()) {
            case R.id.love:
                getFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BeerProvider.Beers.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}

package com.udacity.gerardo.beerroute;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.gerardo.beerroute.data.BeerColumns;
import com.udacity.gerardo.beerroute.data.BeerProvider;
import com.udacity.gerardo.beerroute.data.DatabaseHelperOp;
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.utils.GeneralConst;
import com.udacity.gerardo.beerroute.utils.PaletteTransformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.header_img_poster) ImageView mPhoto;
    @Bind(R.id.beer_origen) TextView mOrigen;
    @Bind(R.id.beer_overview) TextView mOverview;
    @Bind(R.id.beer_alcohol) TextView mAlcohol;
    @Bind(R.id.beer_aroma) TextView mAroma;
    @Bind(R.id.beer_taste) TextView mTaste;
    @Bind(R.id.photo_protection) View mPhotoProtection;
    @Bind(R.id.nested_scroll)
    NestedScrollView mNestedScroll;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsing;
    @Bind(R.id.action_share) FloatingActionButton mShareAction;
    @Bind(R.id.action_love) FloatingActionButton mLoveAction;
    @Bind(R.id.action_route) FloatingActionButton mRouteAction;


    private Beer mBeer;
    private String mBeerId;
    private String mSharedName;
    private static final int DETAIL_LOADER = 0;
    private Uri mUri;
    private static final String[] BEER_COLUMNS = {
            BeerColumns._ID,
            BeerColumns.NAME,
            BeerColumns.OVERVIEW,
            BeerColumns.ORIGEN,
            BeerColumns.IMAGE,
            BeerColumns.ALCOHOL,
            BeerColumns.AROMA,
            BeerColumns.TASTE
    };



    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getActivity().getIntent();
        Bundle arguments = getArguments();
        // Get diferent parameters
        if (intent.getExtras() != null) {
            if (intent.getExtras().containsKey(GeneralConst.BEER_KEY)) {
                mBeer = intent.getParcelableExtra(GeneralConst.BEER_KEY);
                showBeerInfo();
            }
        } else if (arguments != null) {
            if (arguments.containsKey(GeneralConst.BEER_KEY)) {
                mBeer = arguments.getParcelable(GeneralConst.BEER_KEY);
                showBeerInfo();
            } else if (arguments.containsKey(GeneralConst.BEER_URI)) {
                mUri = arguments.getParcelable(GeneralConst.BEER_URI);
            }

        } else if (intent.getData() != null) {
            mUri = intent.getData();
        }

        // Fab buttons
        mRouteAction.setIcon(R.drawable.ic_timeline_white_24dp);
        mRouteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RouteActivity.class);
                i.putExtra(GeneralConst.BEER_KEY, mBeerId);
                Bundle bundle = null;
                // Adding Explode Exit Transition
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setExitTransition(new Explode());
                    bundle = ActivityOptions
                            .makeSceneTransitionAnimation(getActivity()).toBundle();
                }

                getContext().startActivity(i, bundle);
            }
        });
        mShareAction.setIcon(R.drawable.ic_share_variant_white_24dp);
        // Sharing info with external app
        mShareAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mSharedName);
                startActivity(Intent.createChooser(sharingIntent,  getString(R.string.action_share)));

            }
        });
        mLoveAction.setIcon(R.drawable.ic_favorite_white_24dp);
        mLoveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if beer is stored on our database
                Cursor movie = getActivity().getContentResolver().query(BeerProvider.Beers.withId(mBeerId),
                        null, null, null, null);
                // If there isn't data then it means that this beer can be
                // stored
                if (movie.getCount() == 0) {
                    // Changing color to active
                    mLoveAction.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
                    // Insert data using content provider
                    DatabaseHelperOp.insertBeer(mBeer, getActivity());
                }
                // Else we delete the movie
                else {
                    // Changing to clean
                    mLoveAction.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
                    // Delete Data using content provider
                    DatabaseHelperOp.deleteBeer(mBeerId, getActivity());

                }

                movie.close();

            }
        });

        return rootView;
    }

    private void showBeerInfo() {
        mBeerId = mBeer.getId();
        mSharedName = mBeer.getName();
        checkStoredBeer(mBeerId);
        mCollapsing.setTitle(mBeer.getName());
        mOrigen.setText(mBeer.getOrigen());
        mOverview.setText(mBeer.getOverview());
        mAlcohol.setText(mBeer.getAlcohol() + "%");

        List<String> taste = mBeer.getTaste();
        List<String> aroma = mBeer.getAroma();

        String tList = "";
        for(String t : taste) {
            tList += t + " ";
        }
        String aList = "";
        for(String a : aroma) {
            aList += a + " ";
        }

        mAroma.setText(aList);
        mTaste.setText(tList);


        setBackGroundBeer(mBeer.getImage());
    }

    private void setBackGroundBeer(String url) {
        Picasso.with(getContext()).load(url).fit().centerCrop().transform(PaletteTransformation.instance()).into(mPhoto, new Callback.EmptyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) mPhoto.getDrawable()).getBitmap();
                Palette palette = PaletteTransformation.getPalette(bitmap);

                // Adding a Gradient to image in order to get an inmersive experience
                int mutedColor = palette.getLightMutedColor(0xFF333333);
                final int[] colors = {Color.argb(125, 0, 0, 0), mutedColor, mutedColor};
                ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                    @Override
                    public Shader resize(int width, int height) {
                        LinearGradient lg = new LinearGradient(0, 0, 0, mPhotoProtection.getHeight(),
                                colors, //substitute the correct colors for these
                                new float[]{
                                        0, 0.85f, 1},
                                Shader.TileMode.REPEAT);
                        return lg;
                    }
                };
                PaintDrawable p = new PaintDrawable();
                p.setShape(new RectShape());
                p.setShaderFactory(sf);
                //apply the gradient
                mPhotoProtection.setBackground(p);
                mNestedScroll.setBackgroundColor(mutedColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getActivity().getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(palette.getDarkMutedColor(0xFF333333));
                }

                mCollapsing.setBackgroundColor(mutedColor);
                mCollapsing.setContentScrimColor(palette.getMutedColor(mutedColor));
                mCollapsing.setStatusBarScrimColor(palette.getDarkMutedColor(palette.getMutedColor(0xFF333333)));

            }
        });
    }

    private void checkStoredBeer(String id) {
        Cursor beer = getActivity().getContentResolver().query(BeerProvider.Beers.withId(id),
                null, null, null, null);
        if (beer.getCount() > 0) {
            mLoveAction.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            Log.d("OncreateLoader", mUri.toString());
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    BEER_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            mLoveAction.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
            mBeerId = data.getString(data.getColumnIndex(BeerColumns._ID));
            // Get beer data
            String name = data.getString(data.getColumnIndex(BeerColumns.NAME));
            mSharedName = name;
            String origen = data.getString(data.getColumnIndex(BeerColumns.ORIGEN));
            String overview = data.getString(data.getColumnIndex(BeerColumns.OVERVIEW));
            String image = data.getString(data.getColumnIndex(BeerColumns.IMAGE));
            double alcohol = data.getDouble(data.getColumnIndex(BeerColumns.ALCOHOL));
            String aroma = data.getString(data.getColumnIndex(BeerColumns.AROMA));
            String taste = data.getString(data.getColumnIndex(BeerColumns.TASTE));

            insertBeerData(name, origen, overview, image, alcohol, aroma, taste);
        }
    }

    private void insertBeerData(String name, String origen, String overview, String image, double alcohol, String aroma, String taste) {
        // Adding name
        mCollapsing.setTitle(name);
        // Adding origin
        mOrigen.setText(origen);
        // Adding overview
        mOverview.setText(overview);
        // Adding alcohol
        mAlcohol.setText(String.valueOf(alcohol));
        // Adding image
        setBackGroundBeer(image);
        // Adding aroma
        mAroma.setText(aroma);
        // Adding taste
        mTaste.setText(taste);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

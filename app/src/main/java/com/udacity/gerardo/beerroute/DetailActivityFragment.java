package com.udacity.gerardo.beerroute;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
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
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.utils.GeneralConst;
import com.udacity.gerardo.beerroute.utils.PaletteTransformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

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



    public DetailActivityFragment() {
        setHasOptionsMenu(true);
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
//            } else if (arguments.containsKey(GeneralConst.MOVIE_URI)) {
//                mUri = arguments.getParcelable(GeneralConst.MOVIE_URI);
//            }
            }}
//        } else if (intent.getData() != null) {
//            mUri = intent.getData();
//        }

        // Fab buttons
        mRouteAction.setIcon(R.drawable.ic_timeline_white_24dp);
        mRouteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RouteActivity.class);
                i.putExtra(GeneralConst.BEER_KEY, mBeer.getId());
                startActivity(i);
            }
        });
        mShareAction.setIcon(R.drawable.ic_share_variant_white_24dp);
        mLoveAction.setIcon(R.drawable.ic_favorite_white_24dp);

        return rootView;
    }

    private void showBeerInfo() {
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


        Picasso.with(getContext()).load(mBeer.getImage()).fit().centerCrop().transform(PaletteTransformation.instance()).into(mPhoto, new Callback.EmptyCallback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) mPhoto.getDrawable()).getBitmap();
                Palette palette = PaletteTransformation.getPalette(bitmap);

                // Adding a Gradient to image in order to get an inmersive experience
                int mutedColor = palette.getLightMutedColor(0xFF333333);
                final int[] colors = {Color.argb(125, 0, 0, 0),mutedColor,mutedColor};
                ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                    @Override
                    public Shader resize(int width, int height) {
                        LinearGradient lg = new LinearGradient(0, 0, 0, mPhotoProtection.getHeight(),
                                colors, //substitute the correct colors for these
                                new float[] {
                                        0,0.85f,1 },
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
                mCollapsing.setStatusBarScrimColor(palette.getDarkMutedColor( palette.getMutedColor(0xFF333333)));

            }
        });
    }
}

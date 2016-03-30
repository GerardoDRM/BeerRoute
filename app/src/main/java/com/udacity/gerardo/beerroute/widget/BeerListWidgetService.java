package com.udacity.gerardo.beerroute.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;
import com.udacity.gerardo.beerroute.R;
import com.udacity.gerardo.beerroute.data.BeerColumns;
import com.udacity.gerardo.beerroute.data.BeerProvider;
import com.udacity.gerardo.beerroute.utils.CircleTransform;

import java.io.IOException;

/**
 * Created by gerardo on 30/03/16.
 */
public class BeerListWidgetService extends RemoteViewsService {
    private static final String[] MATCH_COLUMNS = {
            BeerColumns._ID,
            BeerColumns.NAME,
            BeerColumns.ORIGEN,
            BeerColumns.IMAGE

    };
    // these indices must match the projection
    private static final int INDEX_ID = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_ORIGIN = 2;
    private static final int INDEX_IMAGE = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                // Get Stored Beers
                Uri beersUri = BeerProvider.Beers.CONTENT_URI;
                data = getContentResolver().query(beersUri, MATCH_COLUMNS, null,
                        null, null, null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.beer_list_widget_item);

                // Extract the beer data from the Cursor
                String beerName = data.getString(INDEX_NAME);
                String beerOrigin = data.getString(INDEX_ORIGIN);
                String beerImage = data.getString(INDEX_IMAGE);

                // Add the data to the RemoteViews
                views.setTextViewText(R.id.beer_title, beerName);
                views.setTextViewText(R.id.beer_origen, beerOrigin);
                try {
                    Bitmap b = Picasso.with(getApplicationContext()).load(beerImage).transform(new CircleTransform()).get();
                    views.setImageViewBitmap(R.id.thumbnail, b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Intent fillInIntent = new Intent();
                Uri beerUri = BeerProvider.Beers.withId(
                        data.getString(INDEX_ID));
                fillInIntent.setData(beerUri);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.beer_list_widget_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}

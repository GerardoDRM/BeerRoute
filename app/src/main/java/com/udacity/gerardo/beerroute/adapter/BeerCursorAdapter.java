package com.udacity.gerardo.beerroute.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.gerardo.beerroute.MainActivityFragment;
import com.udacity.gerardo.beerroute.R;
import com.udacity.gerardo.beerroute.data.BeerColumns;
import com.udacity.gerardo.beerroute.data.BeerProvider;
import com.udacity.gerardo.beerroute.utils.PaletteTransformation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gerardo on 30/03/16.
 * Credit to Sam_chordas https://github.com/schordas/SchematicPlanets/tree/master
 * for all bases
 */
public class BeerCursorAdapter extends CursorRecyclerViewAdapter<BeerCursorAdapter.ViewHolder> {
    private Context mContext;


    public BeerCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;

    }

    @Override
    public void onBindViewHolder(final BeerCursorAdapter.ViewHolder holder, final Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);
        holder.mName.setText(cursor.getString(cursor.getColumnIndex(BeerColumns.NAME)));
        holder.mOrigen.setText(cursor.getString(cursor.getColumnIndex(BeerColumns.ORIGEN)));
        // Get image with Picasso
        Picasso.with(mContext).load(cursor.getString(cursor.getColumnIndex(BeerColumns.IMAGE))).transform(PaletteTransformation.instance()).into(holder.mThumbnail, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) holder.mThumbnail.getDrawable()).getBitmap();
                Palette palette = PaletteTransformation.getPalette(bitmap);
                holder.mView.setBackgroundColor(palette.getMutedColor(0xFF333333));
            }
        });

        final String id = cursor.getString(
                cursor.getColumnIndex(BeerColumns._ID));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivityFragment.CallbackBeer) mContext).onFavoriteSelected(BeerProvider.Beers.withId(id));
            }
        });
    }

    @Override
    public BeerCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_catalog, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.beer_title) TextView mName;
        @Bind(R.id.beer_origen) TextView mOrigen;
        @Bind(R.id.thumbnail) ImageView mThumbnail;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;


        }
    }
}

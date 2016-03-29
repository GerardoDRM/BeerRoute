package com.udacity.gerardo.beerroute.adapter;

import android.content.Context;
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
import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.utils.PaletteTransformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gerardo on 28/03/16.
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private Context mContext;
    private List<Beer> mBeers;

    public CatalogAdapter(Context activity, List<Beer> movies) {
        this.mContext = activity;
        this.mBeers = movies;
    }

    @Override
    public CatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_catalog, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CatalogAdapter.ViewHolder holder, final int position) {
        holder.mName.setText(mBeers.get(position).getName());
        holder.mOrigen.setText(mBeers.get(position).getOrigen());
        // Get image with Picasso
        Picasso.with(mContext).load(mBeers.get(position).getImage()).transform(PaletteTransformation.instance()).into(holder.mThumbnail, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) holder.mThumbnail.getDrawable()).getBitmap();
                Palette palette = PaletteTransformation.getPalette(bitmap);
                holder.mView.setBackgroundColor(palette.getMutedColor(0xFF333333));

            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivityFragment.CallbackBeer) mContext).onItemSelected(mBeers.get(position));
            }
        });
    }

    public void refill(List<Beer> beers) {
        mBeers.clear();
        mBeers.addAll(beers);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBeers.size();
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
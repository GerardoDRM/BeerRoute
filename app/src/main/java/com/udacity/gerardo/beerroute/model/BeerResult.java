package com.udacity.gerardo.beerroute.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerardo de la Rosa on 28/03/16.
 */
public class BeerResult implements Parcelable {

    private List<Beer> beers = new ArrayList<Beer>();

    protected BeerResult(Parcel in) {
        beers = in.createTypedArrayList(Beer.CREATOR);

    }

    public List<Beer> getResults() {
        return beers;
    }

    public void setResults(List<Beer> results) {
        this.beers = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(beers);
    }

    public static final Creator<BeerResult> CREATOR = new Creator<BeerResult>() {
        @Override
        public BeerResult createFromParcel(Parcel in) {
            return new BeerResult(in);
        }

        @Override
        public BeerResult[] newArray(int size) {
            return new BeerResult[size];
        }
    };
}

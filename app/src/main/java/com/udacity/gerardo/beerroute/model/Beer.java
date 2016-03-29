package com.udacity.gerardo.beerroute.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerardo de la Rosa on 28/03/16.
 */
public class Beer implements Parcelable{

    private String image;
    private String id;
    private String origen;
    private String overview;
    private double alcohol;
    private List<String> taste;
    private List<String> aroma;
    private ArrayList mToContinue;
    private ArrayList mToEnd;
    private String name;





    protected Beer(Parcel in) {
        id = in.readString();
        alcohol = in.readDouble();
        image = in.readString();
        name = in.readString();
        origen = in.readString();
        overview = in.readString();
        aroma = in.createStringArrayList();
        taste = in.createStringArrayList();

//        mToContinue = in.readArrayList(null);
//        mToEnd = in.readArrayList(null);

    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(double alcohol) {
        this.alcohol = alcohol;
    }

    public List<String> getTaste() {
        return taste;
    }

    public void setTaste(ArrayList taste) {
        this.taste = taste;
    }

    public List<String> getAroma() {
        return aroma;
    }

    public void setAroma(ArrayList aroma) {
        this.aroma = aroma;
    }

    public ArrayList getmToContinue() {
        return mToContinue;
    }

    public void setmToContinue(ArrayList mToContinue) {
        this.mToContinue = mToContinue;
    }

    public ArrayList getmToEnd() {
        return mToEnd;
    }

    public void setmToEnd(ArrayList mToEnd) {
        this.mToEnd = mToEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(alcohol);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(origen);
        dest.writeString(overview);
        dest.writeStringList(aroma);
        dest.writeStringList(taste);

//        dest.writeList(mToContinue);
//        dest.writeList(mToEnd);

    }

    public static final Creator<Beer> CREATOR = new Creator<Beer>() {
        @Override
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        @Override
        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };
}

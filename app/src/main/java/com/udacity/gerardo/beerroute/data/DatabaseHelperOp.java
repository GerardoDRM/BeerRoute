package com.udacity.gerardo.beerroute.data;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.udacity.gerardo.beerroute.model.Beer;

import java.util.ArrayList;

/**
 * Created by Gerardo de la Rosa on 9/11/15.
 */
public class DatabaseHelperOp {

    private static String LOG_TAG = "DatabaseOp";

    public static void insertBeer(Beer mBeer, Context context) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                BeerProvider.Beers.CONTENT_URI);
        builder.withValue(BeerColumns._ID, mBeer.getId());
        builder.withValue(BeerColumns.NAME, mBeer.getName());
        builder.withValue(BeerColumns.OVERVIEW, mBeer.getOverview());
        builder.withValue(BeerColumns.ORIGEN, mBeer.getOrigen());
        builder.withValue(BeerColumns.IMAGE, mBeer.getImage());
        builder.withValue(BeerColumns.ALCOHOL, mBeer.getAlcohol());
        String a = "";
        for(String aroma: mBeer.getAroma()) {
            a += aroma + " ";
        }
        builder.withValue(BeerColumns.AROMA, a);
        String t = "";
        for(String taste: mBeer.getTaste()) {
            t += taste + " ";
        }
        builder.withValue(BeerColumns.TASTE, t);
        batchOperations.add(builder.build());


        try {
            context.getContentResolver().applyBatch(BeerProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }

    }

    public static void deleteBeer(String id, Context context) {
        Log.d(LOG_TAG, "delete");
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(
                BeerProvider.Beers.withId(id));

        batchOperations.add(builder.build());

        try {
            context.getContentResolver().applyBatch(BeerProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {

            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

}

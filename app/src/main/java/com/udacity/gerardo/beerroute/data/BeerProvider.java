package com.udacity.gerardo.beerroute.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Gerardo de la Rosa on 19/10/15.
 * Credit to SimonVT https://github.com/SimonVT/schematic
 * This app use schematic library to create the content provider
 * it's a simple solution but it has a lot limitations, specially on
 * database actions
 */

@ContentProvider(authority = BeerProvider.AUTHORITY, database = BeerDatabase.class)
public final class BeerProvider {
    public static final String AUTHORITY = "com.udacity.gerardo.beerroute";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String BEERS = "beers";
    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }



    @TableEndpoint(table = BeerDatabase.Tables.BEER) public static class Beers{
        @ContentUri(
                path = Path.BEERS,
                type = "vnd.android.cursor.dir/beers",
                defaultSort = BeerColumns.ALCOHOL + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.BEERS);

        @InexactContentUri(
                name = "BEER_ID",
                path = Path.BEERS + "/*",
                type = "vnd.android.cursor.item/beers",
                whereColumn = BeerColumns._ID,
                pathSegment = 1)
        public static Uri withId(String id) {
            return buildUri(Path.BEERS, id);
        }

    }

}

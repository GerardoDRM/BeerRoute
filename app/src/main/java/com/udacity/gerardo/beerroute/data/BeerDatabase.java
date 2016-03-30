package com.udacity.gerardo.beerroute.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Gerardo de la Rosa on 19/10/15.
 */

@Database(version = BeerDatabase.VERSION)
public final class BeerDatabase {

    private BeerDatabase(){}

    public static final int VERSION = 1;

    public static class Tables {
        @Table(BeerColumns.class) public static final String BEER = "beers";
    }
}

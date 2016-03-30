package com.udacity.gerardo.beerroute.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Gerardo de la Rosa on 19/10/15.
 * Credit to SimonVT https://github.com/SimonVT/schematic
 * This app use schematic library to create the content provider
 * it's a simple solution but it has a lot limitations, specially on
 * database actions
 */
public interface BeerColumns {

    @DataType(DataType.Type.TEXT) @PrimaryKey
    public static final String _ID = "_id";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String ORIGEN = "origen";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String IMAGE = "image";
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String AROMA = "aroma";
    @DataType(DataType.Type.REAL) @NotNull
    public static final String TASTE = "taste";
    @DataType(DataType.Type.REAL) @NotNull
    public static final String ALCOHOL = "alcohol";
}

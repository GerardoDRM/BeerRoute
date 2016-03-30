package com.udacity.gerardo.beerroute.API;

import com.udacity.gerardo.beerroute.model.Beer;
import com.udacity.gerardo.beerroute.model.BeerResult;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by gerardo on 28/03/16.
 */
public interface BeerApi {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter
    @GET("beers/catalog/{page}")
    Call<BeerResult> getBeers(@Path("page") int page);

    @GET("beers/{id}")
    Call<Beer> getBeer(@Path("id") String id);

    @GET("beers/{id}/route")
    Call<Beer> getRoute(@Path("id") String id);
}
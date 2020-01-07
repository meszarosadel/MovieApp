package com.example.movieapp.Interfaces;

import com.example.movieapp.Models.Images;
import com.example.movieapp.Models.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetQueries {
    @GET("movie/popular")
    Call<Page> getPopularMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<Page> searchMovie(@Query("page") int page, @Query("api_key") String apiKey, @Query("query") String query);

    @GET("movie/{movie_id}/similar")
    Call<Page> getSimilar(@Path("movie_id") int id, @Query("page") int page, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/images")
    Call<Images> getImages(@Path("movie_id") int id, @Query("api_key") String apiKey);
}

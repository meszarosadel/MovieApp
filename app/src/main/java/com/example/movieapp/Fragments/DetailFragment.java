package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Adapters.ImageAdapter;
import com.example.movieapp.Adapters.RelatedMovieListAdapter;
import com.example.movieapp.BuildConfig;
import com.example.movieapp.Database.DatabaseHelper;
import com.example.movieapp.Interfaces.GetQueries;
import com.example.movieapp.Models.Images;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.Models.Page;
import com.example.movieapp.R;
import com.example.movieapp.Utils.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailFragment extends Fragment {

    private Movie movie;
    private Call<Page> pageCall;
    private Call<Images> imageCall;

    private RecyclerView recyclerViewGallery;
    private RecyclerView recyclerViewRelatedMovies;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private Button buttonSave;

    private RecyclerView.LayoutManager galleryLayoutManager;
    private RecyclerView.LayoutManager relatedLayoutManager;
    private RelatedMovieListAdapter relatedListAdapter;
    private ImageAdapter galleryAdapter;

    private DatabaseHelper databaseHelper;
    public DetailFragment() {
    }

    public DetailFragment(Movie movie) {
        this.movie = movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail, container, false);

        recyclerViewRelatedMovies = view.findViewById(R.id.rv_related_movies);
        recyclerViewGallery = view.findViewById(R.id.recyclerView_Details_Gallery);
        textViewTitle = view.findViewById(R.id.tv_details_title);
        textViewOverview = view.findViewById(R.id.tv_details_overview);
        buttonSave = view.findViewById(R.id.btn_favortie);

        databaseHelper = databaseHelper.getInstance(getContext());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.addMovie(movie);
            }
        });

        textViewTitle.setText(movie.getTitle());
        textViewOverview.setText(movie.getOverview());

        getImages();
        getSimilarMovies();

        return view;
    }

    private void getImages(){
        GetQueries getQuery = Network.getRetrofit().create(GetQueries.class);

        imageCall = getQuery.getImages(movie.getId(), BuildConfig.API_KEY);

        imageCall.enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                if (galleryAdapter == null) {
                    galleryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    galleryAdapter = new ImageAdapter(response.body(), getContext());
                    recyclerViewGallery.setLayoutManager(galleryLayoutManager);
                    recyclerViewGallery.setAdapter(galleryAdapter);
                } else {
                    galleryAdapter.updateImages(response.body());
                    galleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {

            }
        });
    }

    private void getSimilarMovies(){
        GetQueries getQuery = Network.getRetrofit().create(GetQueries.class);

        pageCall = getQuery.getSimilar(movie.getId(), 1, BuildConfig.API_KEY);

        pageCall.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (relatedListAdapter == null) {
                    relatedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    relatedListAdapter = new RelatedMovieListAdapter(response.body().getResults(), getContext());
                    recyclerViewRelatedMovies.setLayoutManager(relatedLayoutManager);
                    recyclerViewRelatedMovies.setAdapter(relatedListAdapter);
                } else {
                    relatedListAdapter.updateMovies(response.body().getResults());
                    relatedListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }


}

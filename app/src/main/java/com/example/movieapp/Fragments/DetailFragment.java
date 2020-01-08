package com.example.movieapp.Fragments;

import android.content.Context;
import android.net.Uri;
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

    private TextView textViewTitle;
    private TextView textViewOverview;
    private Button buttonSave;



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


        textViewTitle = view.findViewById(R.id.textView_Details_Title);
        textViewOverview = view.findViewById(R.id.textView_Details_Overview);
        buttonSave = view.findViewById(R.id.button_Details_Save);

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
    }

    private void getSimilarMovies(){

    }


}

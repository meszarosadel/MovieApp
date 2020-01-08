package com.example.movieapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movieapp.Adapters.MovieListAdapter;
import com.example.movieapp.Database.DatabaseHelper;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;

import java.util.List;


public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerViewMovies;

    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewTitle;
    private MovieListAdapter adapter;

    DatabaseHelper databaseHelper;
    private static String email;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString("email", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
            newInstance(email);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerViewMovies = view.findViewById(R.id.rv_favoriteMovies);
        textViewTitle = view.findViewById(R.id.tv_favoriteTitle);

        databaseHelper = databaseHelper.getInstance(getContext());

        List<Movie> movies = databaseHelper.getMovies();

        if (adapter == null){
            layoutManager = new LinearLayoutManager(getContext());
            adapter = new MovieListAdapter(movies, getContext());
            recyclerViewMovies.setLayoutManager(layoutManager);
            recyclerViewMovies.setAdapter(adapter);
        }
        else{
            adapter.updateMovies(movies);
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.top_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = null;
        layoutManager = null;
    }

}

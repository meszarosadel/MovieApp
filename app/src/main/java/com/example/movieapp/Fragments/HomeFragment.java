package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Toast;

import com.example.movieapp.Adapters.MovieListAdapter;
import com.example.movieapp.BuildConfig;
import com.example.movieapp.Interfaces.GetQueries;
import com.example.movieapp.Models.Page;
import com.example.movieapp.R;
import com.example.movieapp.Utils.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static String email;
    private SearchView searchView;
    private RecyclerView recyclerViewResults;

    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewTitle;
    private MovieListAdapter adapter;

    private Call<Page> call;
    private String searchQuery;


       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           View view = inflater.inflate(R.layout.fragment_home, container, false);
           Bundle bundle = getArguments();
           if (bundle != null){
               email = getArguments().getString("email");
           }
           searchView = view.findViewById(R.id.sw_homeSearch);
           recyclerViewResults = view.findViewById(R.id.recyclerView_Home_SearchResults);
           textViewTitle = view.findViewById(R.id.textView_Home_Title);

           if (searchQuery == null || searchQuery.isEmpty()){
               loadPopularMovies();
           }
           else{
               searchMovie(searchQuery);
           }
           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String query) {
                   textViewTitle.setText("Search Results");
                   searchQuery = query;
                   searchMovie(query);
                   return true;
               }

               @Override
               public boolean onQueryTextChange(String newText) {
                   return false;
               }
           });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
           setHasOptionsMenu(true);
           super.onCreate(savedInstanceState);
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

    public static String getParameters(){
           return email;

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = null;
        layoutManager = null;
    }

    private void loadPopularMovies(){
        GetQueries getQuery = Network.getRetrofit().create(GetQueries.class);

        call = getQuery.getPopularMovies(1, BuildConfig.API_KEY);

        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (adapter == null){
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new MovieListAdapter(response.body().getResults(), getContext());
                    recyclerViewResults.setLayoutManager(layoutManager);
                    recyclerViewResults.setAdapter(adapter);
                }
                else{
                    adapter.updateMovies(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchMovie(String query){
        GetQueries getQuery = Network.getRetrofit().create(GetQueries.class);

        call = getQuery.searchMovie(1, BuildConfig.API_KEY, query);

        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                if (adapter == null){
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new MovieListAdapter(response.body().getResults(), getContext());
                    recyclerViewResults.setLayoutManager(layoutManager);
                    recyclerViewResults.setAdapter(adapter);

                }
                else{
                    adapter.updateMovies(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }
}


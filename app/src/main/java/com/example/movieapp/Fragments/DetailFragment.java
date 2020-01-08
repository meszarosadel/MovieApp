package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailFragment extends Fragment {

    private Movie movie;
    private List<Movie> favoritMovieList;
    private Call<Page> pageCall;
    private Call<Images> imageCall;

    private RecyclerView rv_detail_images;
    private RecyclerView rv_related_movies;
    private TextView tv_details_title;
    private TextView tv_details_overview;
    private Button btn_favorite, btn_exit;

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

        rv_related_movies = view.findViewById(R.id.rv_related_movies);
        rv_detail_images = view.findViewById(R.id.rv_detail_images);
        tv_details_title = view.findViewById(R.id.tv_details_title);
        tv_details_overview = view.findViewById(R.id.tv_details_overview);
        btn_favorite = view.findViewById(R.id.btn_favortie);
        btn_exit = view.findViewById(R.id.btn_exit);

        databaseHelper = databaseHelper.getInstance(getContext());
        favoritMovieList = databaseHelper.getMovies();
        if (containsId()){
            btn_favorite.setText("DELETE FROM FAVORITE");
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    databaseHelper.deleteMovie(movie.getId());
                    btn_favorite.setText("SAVE AS FAVORITE");

                }
            });
        } else{
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper.addMovie(movie);
                    btn_favorite.setText("DELETE FROM FAVORITE");
            }
            });
        }

        tv_details_title.setText(movie.getTitle());
        tv_details_overview.setText(movie.getOverview());
        getImages();
        getSimilarMovies();

        btn_exit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("email", HomeFragment.getParameters());
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_id, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

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
                    rv_detail_images.setLayoutManager(galleryLayoutManager);
                    rv_detail_images.setAdapter(galleryAdapter);
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
                    rv_related_movies.setLayoutManager(relatedLayoutManager);
                    rv_related_movies.setAdapter(relatedListAdapter);
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

    public boolean containsId(){
        Movie tempMovie;
        for(int i=0; i<favoritMovieList.size();++i){
            tempMovie = favoritMovieList.get(i);
            if(tempMovie.getId()==movie.getId()){
                return true;
            }
        }
        return false;
    }

}

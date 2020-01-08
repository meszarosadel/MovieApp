package com.example.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Fragments.HomeFragment;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    List<Movie> movies;
    Context context;

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public MovieViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView_ListItem);
        }
    }

    public MovieListAdapter(List<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieListAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        ((TextView) holder.cardView.findViewById(R.id.textView_ListItem_Title)).
                setText(movies.get(position).getTitle());
        ((TextView) holder.cardView.findViewById(R.id.textView_ListItem_Overview)).
                setText(movies.get(position).getOverview());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.showDetails(movies.get(position));
            }
        });

        Glide.with(context).
                load("https://image.tmdb.org/t/p/w500" + movies.get(position).getPosterPath()).
                into((ImageView) holder.cardView.findViewById(R.id.imageView_ListItem_Poster));
    }

    @Override
    public int getItemCount() {
        if(movies != null)
        return movies.size();
        return 0;
    }

    public void updateMovies(List<Movie> movies){
        this.movies = movies;
    }


}

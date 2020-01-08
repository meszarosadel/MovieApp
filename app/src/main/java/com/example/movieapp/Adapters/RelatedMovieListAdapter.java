package com.example.movieapp.Adapters;

import android.content.Context;
import android.util.Log;
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

public class RelatedMovieListAdapter extends RecyclerView.Adapter<RelatedMovieListAdapter.RelatedViewHolder> {
    private static final String TAG = "MovieRelatedList";

    List<Movie> movies;
    Context context;

    public class RelatedViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public RelatedViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView_RelatedItem);
        }
    }

    public RelatedMovieListAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_movie_item, parent, false);
        return new RelatedMovieListAdapter.RelatedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        ((TextView) holder.cardView.findViewById(R.id.textView_Related_Title)).
                setText(movies.get(position).getTitle());

        Glide.with(context).
                load("https://image.tmdb.org/t/p/w500" + movies.get(position).getPosterPath()).
                into((ImageView) holder.cardView.findViewById(R.id.imageView_Related_Image));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.showDetails(movies.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> movies){
        this.movies = movies;
    }
}

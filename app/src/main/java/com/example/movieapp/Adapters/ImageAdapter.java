package com.example.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Models.Images;
import com.example.movieapp.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    Images images;
    Context context;

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            this.cardView = view.findViewById(R.id.cardView_GalleryItem);
        }
    }

    public ImageAdapter(Images images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).
                load("https://image.tmdb.org/t/p/w500" + images.getBackdrops().get(position).getFilePath()).
                into((ImageView) holder.cardView.findViewById(R.id.imageView_GalleryItem_Image));
    }

    @Override
    public int getItemCount() {
        return images.getBackdrops().size();
    }

    public void updateImages(Images images){
        this.images = images;
    }
}

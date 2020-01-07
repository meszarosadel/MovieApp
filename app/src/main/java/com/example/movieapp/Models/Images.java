package com.example.movieapp.Models;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Images implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("backdrops")
    private ArrayList<Image> backdrops;

    @SerializedName("posters")
    private ArrayList<Image> posters;

    public Images(int id, ArrayList<Image> backdrops, ArrayList<Image> posters) {
        this.id = id;
        this.backdrops = backdrops;
        this.posters = posters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Image> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Image> backdrops) {
        this.backdrops = backdrops;
    }

    public ArrayList<Image> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Image> posters) {
        this.posters = posters;
    }
}
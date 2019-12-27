package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.movieapp.Fragments.HomeFragment;
import com.example.movieapp.Fragments.LoginFragment;
import com.example.movieapp.Fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_id, new LoginFragment());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String email = HomeFragment.getParameters();
        if(item.getItemId() == R.id.favorit_menu){
            Log.d(MainActivity.class.getSimpleName(), "favorite menu");
        }
        if(item.getItemId() == R.id.profile_menu){
            Fragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putString("email", email);
            fragment.setArguments(args);
            setContentView(R.layout.activity_main);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_id, new ProfileFragment());
            transaction.commit();
        }
        if (item.getItemId() == R.id.cinema_menu){
            Log.d(MainActivity.class.getSimpleName(), "cinema menu");
        }
        if (item.getItemId() == R.id.top_menu){
            Log.d(MainActivity.class.getSimpleName(), "top menu");
        }
        Log.d(MainActivity.class.getSimpleName(), ""+item.getItemId());
        return super.onOptionsItemSelected(item);
        }
}

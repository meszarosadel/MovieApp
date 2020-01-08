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

import com.example.movieapp.Fragments.DetailFragment;
import com.example.movieapp.Fragments.HomeFragment;
import com.example.movieapp.Fragments.LoginFragment;
import com.example.movieapp.Fragments.ProfileFragment;
import com.example.movieapp.Models.Movie;

public class MainActivity extends AppCompatActivity {

    public static FragmentManager mFragmentManager;
    public static String currentUser;
    public static int currentUserID;

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
            transaction.replace(R.id.frame_id, fragment);
            transaction.commit();
        }
        if (item.getItemId() == R.id.top_menu){
            Log.d(MainActivity.class.getSimpleName(), "top menu");
        }
        Log.d(MainActivity.class.getSimpleName(), ""+item.getItemId());
        return super.onOptionsItemSelected(item);
        }

    public static void showDetails(Movie movie){
        mFragmentManager.beginTransaction()
                .replace(R.id.frame_id, new DetailFragment(movie), null)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}

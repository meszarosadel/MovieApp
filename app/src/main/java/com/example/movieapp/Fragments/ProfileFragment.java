package com.example.movieapp.Fragments;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.Database.DatabaseHelper;
import com.example.movieapp.Database.ProfileImage;
import com.example.movieapp.Models.User;
import com.example.movieapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;


public class ProfileFragment extends Fragment {

    ImageView img_v;
    Button btn_save, btn_choose_image;
    TextView tv_password, tv_name, tv_email;
    String email;
    User user;
    DatabaseHelper databaseHelper;
    Bitmap bm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            email = getArguments().getString("email");
        }
        img_v = view.findViewById(R.id.img_v);
        btn_save = view.findViewById(R.id.btn_save);
        btn_choose_image = view.findViewById(R.id.btn_choose_image);
        tv_password = view.findViewById(R.id.tv_password);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        databaseHelper = databaseHelper.getInstance(getContext());
        user = databaseHelper.getUserByEmail(email);
        tv_name.append(user.getUserName());
        tv_email.append(email);

        if(user.getProfilePicture() != null){
            img_v.setImageBitmap(user.getProfilePictureBitmap());
        }


        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }else{
                    startGallery();
                }
            }
        });

        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ResetPasswordFragment();
                Bundle args = new Bundle();
                args.putString("email", email);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_id, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToInternalStorage();

                try {
                    user.setProfilePicture(scaleProfilePicture());
                    databaseHelper.updateUser(user);
                }
                catch (NullPointerException e){
                }
                Fragment fragment = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("email", email);
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
    private void startGallery(){
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1000){
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                img_v.setImageBitmap(bitmapImage);
                bm = bitmapImage;
            }
    }
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

    public void saveToInternalStorage( ){
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
    }

    public boolean loadImageFromStorage(String path, ImageView img)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img.setImageBitmap(b);
            return true;
        }
        catch (FileNotFoundException e)
        {
            return  false;
        }

    }

    private Bitmap scaleProfilePicture(){
        Bitmap image = ((BitmapDrawable) img_v.getDrawable()).getBitmap();
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width/height;

        if (ratio > 1){
            width = 400;
            height = (int) (width / ratio);
        }
        else{
            height = 400;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, false);
    }
}

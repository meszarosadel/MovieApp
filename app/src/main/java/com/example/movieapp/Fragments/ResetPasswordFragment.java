package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.Database.DatabaseHelper;
import com.example.movieapp.Models.User;
import com.example.movieapp.R;


public class ResetPasswordFragment extends Fragment {

    EditText et_new_psw, et_old_psw;
    Button btn_reset;
    DatabaseHelper databaseHelper;
    String email;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            email = getArguments().getString("email");
        }
        et_new_psw = view.findViewById(R.id.et_new_password);
        et_old_psw = view.findViewById(R.id.et_old_psw);
        btn_reset = view.findViewById(R.id.btn_reset);
        databaseHelper = databaseHelper.getInstance(getContext());

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpassword = et_old_psw.getText().toString();
                String newpassword = et_new_psw.getText().toString();
                if (oldpassword.isEmpty() || newpassword.isEmpty()){
                    Toast.makeText(getActivity(),"Fill the fields!", Toast.LENGTH_SHORT).show();
                }
                else if(!(oldpassword.isEmpty() && newpassword.isEmpty()) ){
                    if(databaseHelper.checkUser(email, oldpassword)){
                        user = databaseHelper.getUserByEmail(email);
                        user.setPassword(newpassword);
                        databaseHelper.updateUser(user);
                    }
                }
            }
        });
        return view;
    }

}

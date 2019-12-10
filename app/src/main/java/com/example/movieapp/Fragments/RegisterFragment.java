package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.R;

public class RegisterFragment extends Fragment {

    EditText et_email, et_username,et_password;
    Button btn_register;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container,false);
        et_username = v.findViewById(R.id.et_userName);
        et_email = v.findViewById(R.id.et_email);
        et_password = v.findViewById(R.id.et_password);
        btn_register = v.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(username.isEmpty()){
                    et_username.setError("Please enter your username");
                    et_username.requestFocus();
                }
                else if (email.isEmpty()){
                    et_email.setError("Please enter your email");
                    et_email.requestFocus();
                }
                else if(password.isEmpty()){
                    et_password.setError("Please enter your password");
                    et_password.requestFocus();
                }
                else if(username.isEmpty() && email.isEmpty() && password.isEmpty()){
                    Toast.makeText(getActivity(),"Field are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(username.isEmpty() && email.isEmpty() && password.isEmpty())){
                    //adatbazisos cucok
                }
            }
        });



        return v;
    }


}

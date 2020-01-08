package com.example.movieapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.Database.DatabaseHelper;
import com.example.movieapp.R;


public class LoginFragment extends Fragment {
    TextView tv_goToRegister;
    Button btn_login;
    EditText et_userEmail, et_password;
    DatabaseHelper databaseHelper ;
    public String email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        tv_goToRegister = v.findViewById(R.id.tv_goToRegister);
        btn_login = v.findViewById(R.id.btn_login);
        et_userEmail = v.findViewById(R.id.et_userEmail);
        et_password = v.findViewById(R.id.et_new_password);
        databaseHelper = DatabaseHelper.getInstance(getContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = et_userEmail.getText().toString();
               String password = et_password.getText().toString();
                if (email.isEmpty()){
                    et_userEmail.setError("Please enter your email");
                    et_userEmail.requestFocus();
                }
                else if(password.isEmpty()){
                    et_password.setError("Please enter your password");
                    et_password.requestFocus();
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(getActivity(),"Field are empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && password.isEmpty())){
                    if(databaseHelper.checkUser(email, password)){
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
                    else{
                        Toast.makeText(getActivity(),"Wrong email or password!", Toast.LENGTH_SHORT).show();
                        et_userEmail.setText("");
                        et_password.setText("");
                    }
                }

            }
        });
        tv_goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_id, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return v;
    }


}

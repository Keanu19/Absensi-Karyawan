package com.example.absensikaryawan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.absensikaryawan.api.ApiClient;
import com.example.absensikaryawan.api.ApiInterface;
import com.example.absensikaryawan.model.login.Login;
import com.example.absensikaryawan.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    Connection connect;
    String ConnectionResult;
    ApiInterface apiInterface;
    String usernametext, passwordtext;
    SessionManager sessionManager;
    Button btn_login, btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.editUsername);
        password = findViewById(R.id.editPassword);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_register = findViewById(R.id.btn_daftar);
        btn_register.setOnClickListener(this);

        //Cek Session
        sessionManager = new SessionManager(this);
        if(!sessionManager.isLoggedIn() == false){
            MoveToMainActivity();
        }

    }

    private void MoveToMainActivity() {
        //Ini untuk pindah
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        checkSession();
//    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                usernametext = username.getText().toString();
                passwordtext = password.getText().toString();
                login(usernametext, passwordtext);
            break;
            case  R.id.btn_daftar:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
//        //1.Login dan Save session
//        User user = new User(username.getText().toString() ,password.getText().toString());
//        if (user.getUserID() != -1) {
//
//            SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
//            sessionManagement.saveSession(user);
//            //2.Pindah ke Mainactivity
//            moveToMainActivity();
//        }
//        else {
//            Toast.makeText(this, "Password atau Username salah! "+ user.getUserID(),
//                    Toast.LENGTH_SHORT).show();
//        }

    }


    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkSession() {
        //cek apakah user sudah login
        Boolean status = sessionManager.isLoggedIn();

        if(status != false){
            //jika userID lebih dari -1 maka akan diarahkan ke Main activity
            moveToMainActivity();
        }
        else {//do nothing
        }
    }

//    private void checkSession() {
//        //cek apakah user sudah login
//        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
//        int userID = sessionManagement.getSession();
//
//        if(userID != -1){
//            //jika userID lebih dari -1 maka akan diarahkan ke Main activity
//            moveToMainActivity();
//        }
//        else {//do nothing
//        }
//    }


    private void login(String username, String password) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username,password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    // Ini untuk menyimpan sesi
                    sessionManager = new SessionManager(LoginActivity.this);
                    LoginData loginData = response.body().getLoginData();
                    sessionManager.createLoginSession(loginData);

                    //Ini untuk pindah
                    Toast.makeText(LoginActivity.this, response.body().getLoginData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
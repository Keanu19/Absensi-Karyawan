package com.example.absensikaryawan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.absensikaryawan.api.ApiClient;
import com.example.absensikaryawan.api.ApiInterface;
import com.example.absensikaryawan.model.Update.Update;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{

    EditText  etPassword, etName, etDivisi, etJobdesk;
    TextView txUpdateUsername;
    Button btnUpdate;
    String Username, Password, Name, Divisi, Jobdesk;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txUpdateUsername = findViewById(R.id.txUpdateUsername);
        etPassword = findViewById(R.id.etUpdatePassword);
        etName = findViewById(R.id.etUpdateName);
        etDivisi = findViewById(R.id.etUpdateDivisi);
        etJobdesk = findViewById(R.id.etUpdateJobdesk);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        sessionManager = new SessionManager(UpdateActivity.this);

        //Inisisasi nama dan username

            Username = sessionManager.getUserDetail().get(SessionManager.USERNAME);
            txUpdateUsername.setText(Username);


            Divisi = sessionManager.getUserDetail().get(SessionManager.DIVISI);
            etDivisi.setText(Divisi);


            Jobdesk = sessionManager.getUserDetail().get(SessionManager.JOBDESK);
            etJobdesk.setText(Jobdesk);


            Name = sessionManager.getUserDetail().get(SessionManager.NAME);
            etName.setText(Name);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdate:
                Username = txUpdateUsername.getText().toString();
                Password = etPassword.getText().toString();
                Name = etName.getText().toString();
                Divisi = etDivisi.getText().toString();
                Jobdesk = etJobdesk.getText().toString();
                update(Username, Password, Name, Jobdesk, Divisi);
                break;
        }
    }

    private void update(String username, String password, String name, String jobdesk, String divisi) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Update> call = apiInterface.updateResponse(username, password, name, jobdesk, divisi);
        call.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if(response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    //startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(UpdateActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
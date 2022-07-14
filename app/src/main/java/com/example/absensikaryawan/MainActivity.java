package com.example.absensikaryawan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.sql.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    Button buttonMasuk, buttonKeluar, buttonDaftarWajah;
    TextView textName;
    TextClock textTime,textDate;
//    SimpleDateFormat dateFormat, timeFormat;
//    Calendar calender;
//    String date;
//    String time;
    String name;
    SessionManager sessionManager;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMasuk = findViewById(R.id.buttonMasuk);
        buttonKeluar = findViewById(R.id.buttonKeluar);
        buttonDaftarWajah = findViewById(R.id.buttonDaftarWajah);
        textName = findViewById(R.id.textName);
        textTime = findViewById(R.id.textTime);
        textDate = findViewById(R.id.textDate);

//        calender = Calendar.getInstance();

        //Tombol Masuk
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,OpenCamera.class));
            }
        });

        //menampilkan nama user dari sessionmanager
        //name = sessionManager.getUserDetail().get(SessionManager.USERNAME);
        textName.setText("Keanu");

        this.textDate.setFormat12Hour(null);
        this.textTime.setFormat12Hour(null);

    }

    public void logout(View view) {
        //User agar bisa Logut
        sessionManager = new SessionManager(MainActivity.this);
        //Remove session
        sessionManager.logoutSession();
        //pindah ke Login page
        MoveToLogin();
    }

//    public void logout(View view) {
//        //User agar bisa Logut
//        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
//        //Remove session
//        sessionManagement.removeSession();
//        //pindah ke Login page
//        MoveToLogin();
//    }

    private void MoveToLogin() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    private void displayTimeDate(){
//
//        //Text waktu
//        timeFormat = new SimpleDateFormat("HH:mm");
//        time = timeFormat.format((calender.getTime()));
//        textTime.setText(time);
//
//        //Text tanggal
//        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        date = dateFormat.format((calender.getTime()));
//        textDate.setText(date);
//    }

}
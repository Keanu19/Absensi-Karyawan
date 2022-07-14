package com.example.absensikaryawan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    //Tabel user
    public static final String TABEL_USER = "user";
    public static final String COLUMN_ID = "userID";
    public static final String COLUMN_USERNAME = "userName";
    public static final String COLUMN_JOBDESK = "jobDesk";
    public static final String COLUMN_DIVISI = "divisi";
    //tabel absensi
    public static final String TABEL_ABSENSI = "absensi";
    public static final String COLUMN_ABSEN_ID = "absenID";
    public static final String COLUMN_ABSEN_MASUK = "absenMasuk";
    public static final String COLUMN_ABSEN_KELUAR = "absenKeluar";
    public static final String COLUMN_TANGGAL_ABSEN = "tanggalAbsen";
    public static final String COLUMN_BULAN_ABSEN = "bulanAbsen";
    public static final String COLUMN_TAHUN_ABSEN = "tahunAbsen";
    public static final String COLUMN_USER_ID = "userUserID";

    public Database(@Nullable Context context) {
        super(context, "absensi.db", null, 2);
    }

    //mengakses database pertamakali
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatements = "CREATE TABLE " + TABEL_USER + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " + COLUMN_JOBDESK + " TEXT, " + COLUMN_DIVISI + " TEXT)";
        //create second table
        String createTableStatements2 = "CREATE TABLE " + TABEL_ABSENSI + " (" + COLUMN_ABSEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ABSEN_MASUK + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_ABSEN_KELUAR + " DATETIME DEFAULT CURRENT_TIMESTAMP," + COLUMN_TANGGAL_ABSEN + " TEXT, " + COLUMN_BULAN_ABSEN + " TEXT, " + COLUMN_TAHUN_ABSEN + " TEXT, " +
                COLUMN_USER_ID + " TEXT)";

        db.execSQL(createTableStatements2);
        db.execSQL(createTableStatements);
    }
    //database version management
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTableStatements = "CREATE TABLE " + TABEL_USER + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " + COLUMN_JOBDESK + " TEXT, " + COLUMN_DIVISI + " TEXT)";
        //create second table
        String createTableStatements2 = "CREATE TABLE " + TABEL_ABSENSI + " (" + COLUMN_ABSEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ABSEN_MASUK + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                COLUMN_ABSEN_KELUAR + " DATETIME DEFAULT CURRENT_TIMESTAMP," + COLUMN_TANGGAL_ABSEN + " TEXT, " + COLUMN_BULAN_ABSEN + " TEXT, " + COLUMN_TAHUN_ABSEN + " TEXT, " +
                COLUMN_USER_ID + " TEXT)";

        db.execSQL(createTableStatements2);
        db.execSQL(createTableStatements);
    }



}

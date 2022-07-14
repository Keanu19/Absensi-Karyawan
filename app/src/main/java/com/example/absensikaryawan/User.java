package com.example.absensikaryawan;

import android.content.Context;
import android.widget.Toast;

import androidx.core.database.DatabaseUtilsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class User {
    String SERVER = "192.168.18.6";
    String LOGIN = "/absensi/login.php";
    int userID = -1;
    String username;
    String password;
    Connection connect;
    Context context;


    public User(String username, String password) {
        this.userID = -1;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        RequestQueue queue = Volley.newRequestQueue(context);

        return userID;
    }
}

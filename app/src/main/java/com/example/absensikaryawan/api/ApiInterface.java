package com.example.absensikaryawan.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.example.absensikaryawan.model.Update.Update;
import com.example.absensikaryawan.model.login.Login;
import com.example.absensikaryawan.model.register.Register;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("jobdesk") String jobdesk,
            @Field("divisi") String divisi
    );


    @FormUrlEncoded
    @POST("update.php")
    Call<Update> updateResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("jobdesk") String jobdesk,
            @Field("divisi") String divisi
    );

}

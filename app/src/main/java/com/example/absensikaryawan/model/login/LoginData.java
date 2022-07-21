package com.example.absensikaryawan.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("divisi")
    private String divisi;

    @SerializedName("jobdesk")
    private String jobdesk;

    @SerializedName("faceid")
    private String faceid;

    //GETTER AND SETTER

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    public String getJobdesk() {
        return jobdesk;
    }

    public void setJobdesk(String jobdesk) {
        this.jobdesk = jobdesk;
    }

    public String getFaceid() {
        return faceid;
    }

    public void setFaceid(String faceid) {
        this.faceid = faceid;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
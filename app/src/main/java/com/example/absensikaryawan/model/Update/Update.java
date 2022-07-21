package com.example.absensikaryawan.model.Update;

import com.google.gson.annotations.SerializedName;

public class Update {

    @SerializedName("data")
    private UpdateData updateData;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public void setUpdateData(UpdateData updateData){
        this.updateData = updateData;
    }

    public UpdateData getUpdateData(){
        return updateData;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public boolean isStatus(){
        return status;
    }
}

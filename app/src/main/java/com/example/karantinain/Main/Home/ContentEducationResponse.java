package com.example.karantinain.Main.Home;

import java.util.List;

import com.example.karantinain.Main.Home.ContentEducationData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentEducationResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ContentEducationData> data = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ContentEducationData> getData() {
        return data;
    }

    public void setData(List<ContentEducationData> data) {
        this.data = data;
    }

}
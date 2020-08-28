package com.example.karantinain.Main.Home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelfieResponse {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SelfieData data;

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

    public SelfieData getData() {
        return data;
    }

    public void setData(SelfieData data) {
        this.data = data;
    }
}

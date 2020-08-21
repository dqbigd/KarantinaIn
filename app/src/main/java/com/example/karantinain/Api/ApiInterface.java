package com.example.karantinain.Api;

import com.example.karantinain.Login.ForgotPassword.ForgotPasswordResponse;
import com.example.karantinain.Login.LoginResponse;
import com.example.karantinain.Login.ProfileResponse;
import com.example.karantinain.Main.Home.ContentEducationResponse;
import com.example.karantinain.Main.Home.RecommendActivityResponse;
import com.example.karantinain.Register.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface ApiInterface {
    
    //Register
    @FormUrlEncoded
    @POST("/auth/signup")
    Call<RegisterResponse> signUp(@Field("fullname") String fullName,
                                    @Field("email") String email,
                                    @Field("username") String username,
                                    @Field("password") String password,
                                    @Field("phoneNumber") String phoneNumber,
                                    @Field("gender") String gender,
                                    @Field("age") String age,
                                    @Field("indication") String indication);

    //Login
    @FormUrlEncoded
    @POST("auth/signin")
    Call<LoginResponse> signIn(@Field("username") String username,
                              @Field("password") String password);

    //Send Email(Forgot Password)
    @FormUrlEncoded
    @PUT("/auth/forgot-password")
    Call<ForgotPasswordResponse> sendEmail(@Field("email") String email);

    //Password Change(Forgot Password)
    @FormUrlEncoded
    @PUT("/auth/reset-password")
    Call<ForgotPasswordResponse> passwordChange(@Field("email") String email,
                                                @Field("token") String token,
                                                @Field("password") String password);

    //Profile
    @GET("/user/profile")
    Call<ProfileResponse> profile(@Header("Authorization") String token);

    //Recommend Activity (Home)
    @GET("/activity")
    Call<RecommendActivityResponse> recommendActivity(@Header("Authorization") String token);

    //Content Education (Home)
    @GET("/education")
    Call<ContentEducationResponse> contentEducation(@Header("Authorization") String token);

//    //LUG API Schedule Today
//    @GET("/schedule/today")
//    Call<ScheduleResponse> scheduleToday(@Header("Authorization") String token);
//
//    //LUG API Schedule Weekly
//    @GET("/schedule/weekly")
//    Call<ScheduleResponse> scheduleWeekly(@Header("Authorization") String token);
//
//    //LUG API Schedule Mid (UTS)
//    @GET("/schedule/midterm-exam")
//    Call<ExamScheduleResponse> scheduleExamMid(@Header("Authorization") String token);
//
//    //LUG API Schedule Final (UAS)
//    @GET("/schedule/final-exam")
//    Call<ExamScheduleResponse> scheduleExamFinal(@Header("Authorization") String token);
//
//    //LUG API SSKM
//    @GET("/points")
//    Call<PointsResponse> points(@Header("Authorization") String token);
//
//    //LUG API Detail SSKM
//    @GET
//    Call<PointsDetailResponse> pointsDetail(@Header("Authorization") String token, @Url String url);

}

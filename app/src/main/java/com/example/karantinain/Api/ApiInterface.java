package com.example.karantinain.Api;

import com.example.karantinain.Login.ForgotPassword.ForgotPasswordResponse;
import com.example.karantinain.Login.LoginResponse;
import com.example.karantinain.Login.ProfileResponse;
import com.example.karantinain.Main.Chat.MessageResponse;
import com.example.karantinain.Main.Chat.SendMessageResponse;
import com.example.karantinain.Main.Home.ContentEducationResponse;
import com.example.karantinain.Main.Home.LocationResponse;
import com.example.karantinain.Main.Home.RecommendActivityResponse;
import com.example.karantinain.Main.Home.SelfieResponse;
import com.example.karantinain.Main.Insight.FoodResponse;
import com.example.karantinain.Main.Insight.VideoResponse;
import com.example.karantinain.Register.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
                                  @Field("indication") String indication,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude);

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

    //Selfie (Home)
    @FormUrlEncoded
    @POST("/selfie")
    Call<SelfieResponse> selfie(@Header("Authorization") String token,
                                  @Field("image") String image);

    //Location GPS (Home)
    @FormUrlEncoded
    @POST("/location")
    Call<LocationResponse> location(@Header("Authorization") String token,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude);

    //Recommend Activity (Home)
    @GET("/activity")
    Call<RecommendActivityResponse> recommendActivity(@Header("Authorization") String token);

    //Content Education (Home)
    @GET("/education")
    Call<ContentEducationResponse> contentEducation(@Header("Authorization") String token);

    //Video (Insight)
    @GET("/youtube")
    Call<VideoResponse> video(@Header("Authorization") String token);

    //Food (Insight)
    @GET("/food")
    Call<FoodResponse> food(@Header("Authorization") String token);

    //Food (Insight)
    @GET("/chat")
    Call<MessageResponse> getMessage(@Header("Authorization") String token);

    //Send Message (Chat)
    @FormUrlEncoded
    @POST("/chat")
    Call<SendMessageResponse> sendMessage(@Header("Authorization") String token,
                                          @Field("message") String message);

//    //LUG API Detail SSKM
//    @GET
//    Call<PointsDetailResponse> pointsDetail(@Header("Authorization") String token, @Url String url);

}

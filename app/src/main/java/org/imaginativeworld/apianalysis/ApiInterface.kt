package org.imaginativeworld.apianalysis

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // Get Notification
    @GET("daily_report/{id}")
    fun dogetNotification(@Path("id") id: Int): Call<ResponseBody>

    // Login
    @FormUrlEncoded
    @POST("login")
    fun doUserLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

//    @FormUrlEncoded
//    @POST("auth/login")
//    fun doUserLogin(
//        @Field("username") username: String,
//        @Field("password") password: String
//    ): Call<ResponseBody>

}
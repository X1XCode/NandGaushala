package com.gaushala.webservices


import com.gaushala.model.dashboard.MessageUserResponse
import com.gaushala.model.dashboard.SendMessageResponse
import com.gaushala.model.dashboard.TokenResponse
import com.gaushala.model.dashboard.UserSearchResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface WebApi {
    companion object {
        const val BASE_URL = "http://122.169.105.195:8089/"
    }

    @FormUrlEncoded
    @POST("token")
    fun getToken(@Field("grant_type") grantType: String, @Field("username") username: String,
                 @Field("password") password: String, @Field("refresh_token") refresh_token: String ): Call<TokenResponse>

    @GET("api/GetUserSearch?")
    fun getUserSearch(@Query("userName") userName: String, @Query("desingation") designation: String,
                      @Query("department") department: String): Call<UserSearchResponse>

    @POST("api/SendMessage")
    fun sendMessage(@Body jsonObject: JsonObject) : Call<SendMessageResponse>

    @GET("api/GetMessageUser")
    fun getMessageUser() : Call<MessageUserResponse>
}
package com.awp.samakaki.data

import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.PostRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.*
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import com.awp.samakaki.response.UserRelationsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("posts")
    suspend fun getAllPosts(): Response<PostsResponse>

    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @Multipart
    @POST("posts")
    suspend fun createPosts(
        @Header("Authorization") token: String,
        @Part("descriptions") descriptions: RequestBody,
        @Part("status") status: RequestBody,
        @Part content: MultipartBody.Part
        ): Response<PostsResponse>

    @Multipart
    @POST("biodata_users")
    suspend fun createBiodata(
        @Header("Authorization") token: String,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody,
        @Part("marriage_status") marriage_status: RequestBody,
        @Part("status") status: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<BiodataResponse>

    @GET("user_relations")
    suspend fun userRelations(
        @Header("Authorization") token: String
    ): Response<UserRelationsResponse>



}
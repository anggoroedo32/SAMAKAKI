package com.awp.samakaki.data

import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import com.awp.samakaki.response.UserRelationsResponse
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

    @POST("posts")
    suspend fun createPosts(postItem: String): Response<PostsResponse>
    @GET("user_relations")
    suspend fun userRelations(
        @Header("Authorization") token: String
    ): Response<UserRelationsResponse>



}
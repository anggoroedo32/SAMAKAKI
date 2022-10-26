package com.awp.samakaki.data

import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.Data
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    suspend fun createFamily(

    )


}
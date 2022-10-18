package com.awp.samakaki.data

import com.awp.samakaki.response.PostsResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("posts")
    suspend fun getAllPost(): Call<PostsResponse>


}
package com.awp.samakaki.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import com.awp.samakaki.data.ApiService
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.Data
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPosts(): Response<PostsResponse> = apiService.getAllPosts()
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> = apiService.register(registerRequest)

}
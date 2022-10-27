package com.awp.samakaki.repository

import com.awp.samakaki.data.ApiService
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPosts(): Response<PostsResponse> = apiService.getAllPosts()
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> =
        apiService.register(registerRequest)

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> =
        apiService.login(loginRequest)

}
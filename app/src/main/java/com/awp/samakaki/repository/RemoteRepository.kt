package com.awp.samakaki.repository

import com.awp.samakaki.data.ApiService
import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.*
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import com.awp.samakaki.response.UserRelationsResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllPosts(): Response<PostsResponse> = apiService.getAllPosts()
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> = apiService.register(registerRequest)
    suspend fun createPosts(postItem: String): Response<PostsResponse> = apiService.createPosts(postItem)
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> = apiService.login(loginRequest)
    suspend fun createBiodata(token: String,biodataRequest: BiodataRequest): Response<BiodataResponse> = apiService.createBiodata(token,biodataRequest)
    suspend fun userRelations(token: String): Response<UserRelationsResponse> = apiService.userRelations(token)
}
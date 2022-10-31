package com.awp.samakaki.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import com.awp.samakaki.data.ApiService
import com.awp.samakaki.request.ForgotTokenRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.request.ResetPasswordRequest
import com.awp.samakaki.response.*
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllPosts(): Response<PostsResponse> = apiService.getAllPosts()
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> = apiService.register(registerRequest)
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> = apiService.login(loginRequest)
    suspend fun forgotToken(forgotTokenRequest: ForgotTokenRequest): Response<ForgotTokenResponse> = apiService.forgotToken(forgotTokenRequest)
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse> = apiService.resetPassword(resetPasswordRequest)
}
package com.awp.samakaki.repository

import com.awp.samakaki.data.ApiService
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.request.ForgotTokenRequest
import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.PostRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.request.*
import com.awp.samakaki.response.*
import com.awp.samakaki.response.*
import retrofit2.Call
import com.awp.samakaki.response.*
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllPosts(): Response<PostsResponse> = apiService.getAllPosts()
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> = apiService.register(registerRequest)
    suspend fun createPosts(token: String,
                            descriptions: RequestBody,
                            status: RequestBody,
                            content: MultipartBody.Part): Response<PostsResponse> = apiService.createPosts(token, descriptions, status, content)
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> = apiService.login(loginRequest)
    suspend fun createBiodata(token: String,
                              dob: RequestBody,
                              address: RequestBody,
                              marriage_status: RequestBody,
                              status: RequestBody,
                              file: MultipartBody.Part): Response<BiodataResponse> = apiService.createBiodata(
        token,
        dob,
        address,
        marriage_status,
        status,
        file)
    suspend fun findUser(token: String, id: String): Response<FindUserResponse> = apiService.findUser(token, id)
    suspend fun findUserRelations(token: String): Response<GetUserRelationResponse> = apiService.findUserRelations(token)
    suspend fun createUserRelations(token: String, createRelationsRequest: CreateRelationsRequest): Response<CreateRelationsResponse> = apiService.createUserRelations(token, createRelationsRequest)
    suspend fun createFamilyTree(token: String, createFamilyTreeRequest: CreateFamilyTreeRequest): Response<CreateFamilyTreeResponse> = apiService.createFamilyTree(token, createFamilyTreeRequest)
    suspend fun registerWithToken(registerWithTokenRequest: RegisterWithTokenRequest): Response<RegisterWithInvitationResponse> = apiService.registerWithToken(registerWithTokenRequest)
    suspend fun forgotToken(forgotTokenRequest: ForgotTokenRequest): Response<ForgotTokenResponse> = apiService.forgotToken(forgotTokenRequest)
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse> = apiService.resetPassword(resetPasswordRequest)
}
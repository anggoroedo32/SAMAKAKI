package com.awp.samakaki.data

import com.awp.samakaki.request.ForgotTokenRequest
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.request.ResetPasswordRequest
import com.awp.samakaki.response.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import com.awp.samakaki.response.*
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

    @POST("password/forgot")
    suspend fun forgotToken(
        @Body forgotTokenRequest: ForgotTokenRequest
    ): Response<ForgotTokenResponse>

    @Headers("Content-Type: application/json")
    @GET("user_relations")
    suspend fun findUserRelations(
        @Header("Authorization") token: String
    ): Response<UserRelationsResponse>

    @POST("password/reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResetPasswordResponse>
    @GET("users")
    suspend fun findUser(
        @Header("Authorization") token: String,
        @Body userRequest: UserRequest
    ): Response<FindUserResponse>


    @POST("relations")
    suspend fun createUserRelations(
        @Header("Authorization") token: String,
        @Body createRelationsRequest: CreateRelationsRequest
    ): Response<CreateUserRelationsResponse>

    @POST("family_trees")
    suspend fun createFamilyTree(
        @Header("Authorization") token: String,
        @Body createFamilyTreeRequest: CreateFamilyTreeRequest
    ): Response<CreateFamilyTreeResponse>

}
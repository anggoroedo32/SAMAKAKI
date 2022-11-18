package com.awp.samakaki.data

import com.awp.samakaki.request.ForgotTokenRequest
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.PostRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.request.ResetPasswordRequest
import com.awp.samakaki.request.*
import com.awp.samakaki.response.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import com.awp.samakaki.response.*
import com.awp.samakaki.response.*
import com.awp.samakaki.response.LoginResponse
import com.awp.samakaki.response.PostsResponse
import com.awp.samakaki.response.RegisterResponse
import okhttp3.MultipartBody
import com.awp.samakaki.response.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

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

    @Multipart
    @POST("posts")
    suspend fun createPosts(
        @Header("Authorization") token: String,
        @Part("descriptions") descriptions: RequestBody,
        @Part("status") status: RequestBody,
        @Part content: MultipartBody.Part?
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
    suspend fun findUserRelations(
        @Header("Authorization") token: String
    ): Response<UserRelationsResponse>

    @GET("notifications")
    suspend fun getNotificationByUser(
        @Header("Authorization") token: String
    ): Response<NotificationsResponse>

    @PUT("accepted/invitation")
    suspend fun updateRelation(
        @Header("Authorization") token: String,
        @Query("token") invitationToken: String,
        @Body updateRelationRequest: UpdateRelationRequest
    ): Response<UpdateRelationsResponse>

    @POST("password/reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @GET("biodata_users/{id}")
    suspend fun findUser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<FindUserResponse>

    @Multipart
    @PUT("biodata_users/{id}")
    suspend fun  editProfile(
        @Header("Authorization") token: String,
        @Path ("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody,
        @Part("marriage_status") marriageStatus: RequestBody,
        @Part("status") status: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<EditProfileResponse>

    @POST("invitation/register")
    suspend fun registerWithToken(
        @Body registerWithTokenRequest: RegisterWithTokenRequest
    ): Response<RegisterWithInvitationResponse>

    @POST("relations")
    suspend fun createUserRelations(
        @Header("Authorization") token: String,
        @Body createRelationsRequest: CreateRelationsRequest
    ): Response<CreateRelationResponse>

    @POST("family_trees")
    suspend fun createFamilyTree(
        @Header("Authorization") token: String,
        @Body createFamilyTreeRequest: CreateFamilyTreeRequest
    ): Response<CreateFamilyTreeResponse>

    @POST("invite/users")
    suspend fun inviteFamily(
        @Header("Authorization") token: String,
        @Body inviteFamilyRequest: InviteFamilyRequest
    ): Response<InviteFamilyResponse>

    @GET("posts")
    suspend fun getAllPostsByFamily(
        @Header("Authorization") token: String
    ): Response<NewPostsResponse>

    @GET("user/posts")
    suspend fun getAllPostsByUser(
        @Header("Authorization") token: String
    ): Response<PostUserResponse>
}
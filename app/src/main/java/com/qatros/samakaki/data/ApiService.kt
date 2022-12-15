package com.qatros.samakaki.data

import com.qatros.samakaki.request.ForgotTokenRequest
import com.qatros.samakaki.request.CreateFamilyTreeRequest
import com.qatros.samakaki.request.CreateRelationsRequest
import com.qatros.samakaki.request.LoginRequest
import com.qatros.samakaki.request.RegisterRequest
import com.qatros.samakaki.request.ResetPasswordRequest
import com.qatros.samakaki.request.*
import com.qatros.samakaki.response.*
import okhttp3.RequestBody
import com.qatros.samakaki.response.LoginResponse
import com.qatros.samakaki.response.PostsResponse
import com.qatros.samakaki.response.RegisterResponse
import okhttp3.MultipartBody
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

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DeletePostResponse>

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

    @Multipart
    @PUT("biodata_users/{id}")
    suspend fun editPrivacy(
        @Header("Authorization") token: String,
        @Path ("id") id: Int,
        @Part("status") status: RequestBody
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
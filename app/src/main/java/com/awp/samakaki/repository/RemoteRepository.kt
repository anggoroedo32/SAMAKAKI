package com.awp.samakaki.repository

import android.util.Log
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
    suspend fun getAllPostsByFamily(token: String): Response<NewPostsResponse> = apiService.getAllPostsByFamily(token)
    suspend fun getAllPostsByUser(token: String): Response<PostUserResponse> = apiService.getAllPostsByUser(token)
    suspend fun register(registerRequest: RegisterRequest): Response<RegisterResponse> = apiService.register(registerRequest)
    suspend fun createPosts(token: String,
                            descriptions: RequestBody,
                            status: RequestBody,
                            content: MultipartBody.Part?): Response<PostsResponse> = apiService.createPosts(token, descriptions, status, content)
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
        file
    )

    suspend fun editProfile(token: String,
                            id: Int,
                            dob: RequestBody,
                            name: RequestBody,
                            email: RequestBody,
                            phone: RequestBody,
                            address: RequestBody,
                            marriage_status: RequestBody,
                            status: RequestBody,
                            file: MultipartBody.Part?
    ): Response<EditProfileResponse> = apiService.editProfile(
        token,
        id,
        name,
        email,
        phone,
        dob,
        address,
        marriage_status,
        status,
        file
    )

    suspend fun findUser(token: String, id: String): Response<FindUserResponse> = apiService.findUser(token, id)
    suspend fun findUserRelations(token: String): Response<UserRelationsResponse> = apiService.findUserRelations(token)
    suspend fun getNotificationByUser(token: String): Response<NotificationsResponse> = apiService.getNotificationByUser(token)
    suspend fun updateRelation(token: String, invitationToken: String, updateRelationRequest: UpdateRelationRequest): Response<UpdateRelationsResponse> = apiService.updateRelation(token = token, invitationToken = invitationToken, updateRelationRequest)
    suspend fun createUserRelations(token: String, createRelationsRequest: CreateRelationsRequest): Response<CreateRelationResponse> = apiService.createUserRelations(token, createRelationsRequest)
    suspend fun createFamilyTree(token: String, createFamilyTreeRequest: CreateFamilyTreeRequest): Response<CreateFamilyTreeResponse> = apiService.createFamilyTree(token, createFamilyTreeRequest)
    suspend fun inviteFamily(token: String, inviteFamilyRequest: InviteFamilyRequest): Response<InviteFamilyResponse> = apiService.inviteFamily(token, inviteFamilyRequest)
    suspend fun registerWithToken(registerWithTokenRequest: RegisterWithTokenRequest): Response<RegisterWithInvitationResponse> = apiService.registerWithToken(registerWithTokenRequest)
    suspend fun forgotToken(forgotTokenRequest: ForgotTokenRequest): Response<ForgotTokenResponse> = apiService.forgotToken(forgotTokenRequest)
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse> = apiService.resetPassword(resetPasswordRequest)


}
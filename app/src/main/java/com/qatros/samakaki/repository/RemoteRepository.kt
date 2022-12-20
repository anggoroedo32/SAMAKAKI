package com.qatros.samakaki.repository

import com.qatros.samakaki.data.ApiService
import com.qatros.samakaki.request.CreateFamilyTreeRequest
import com.qatros.samakaki.request.CreateRelationsRequest
import com.qatros.samakaki.request.ForgotTokenRequest
import com.qatros.samakaki.request.LoginRequest
import com.qatros.samakaki.request.RegisterRequest
import com.qatros.samakaki.request.*
import com.qatros.samakaki.response.*
import com.qatros.samakaki.response.LoginResponse
import com.qatros.samakaki.response.PostsResponse
import com.qatros.samakaki.response.RegisterResponse
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

    suspend fun resendEmailConfirmation(token: String): Response<ResendEmailResponse> = apiService.resendEmailConfirmation(token)

    suspend fun deletePost(token: String, id: Int): Response<DeletePostResponse> = apiService.deletePost(token, id)

    suspend fun editPrivacy(token: String, id: Int, status: RequestBody): Response<EditProfileResponse> = apiService.editPrivacy(token, id, status)

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
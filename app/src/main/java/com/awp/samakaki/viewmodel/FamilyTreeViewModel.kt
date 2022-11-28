package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.helper.SingleLiveEvent
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.request.InviteFamilyRequest
import com.awp.samakaki.request.UpdateRelationRequest
import com.awp.samakaki.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class FamilyTreeViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _findUserRelations = MutableLiveData<BaseResponse<UserRelationsResponse>>()
    val findUserRelations: LiveData<BaseResponse<UserRelationsResponse>> = _findUserRelations

    private val _createUserRelations = MutableLiveData<SingleLiveEvent<BaseResponse<CreateRelationResponse>>>()
    val createUserRelations: LiveData<SingleLiveEvent<BaseResponse<CreateRelationResponse>>> = _createUserRelations

    private val _updateRelations = MutableLiveData<BaseResponse<UpdateRelationsResponse>>()
    val updateRelations: LiveData<BaseResponse<UpdateRelationsResponse>> = _updateRelations

    private val _createFamilyTree = MutableLiveData<BaseResponse<CreateFamilyTreeResponse>>()
    val createFamilyTree: LiveData<BaseResponse<CreateFamilyTreeResponse>> = _createFamilyTree

    private val _inviteFamily = MutableLiveData<BaseResponse<InviteFamilyResponse>>()
    val inviteFamily: LiveData<BaseResponse<InviteFamilyResponse>> = _inviteFamily

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun findUserRelations(token: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.findUserRelations(token)
                if (response.code() == 200){
                    _findUserRelations.value = BaseResponse.Success(response.body())
                    _loading.value = false
                } else {
                    _findUserRelations.value = BaseResponse.Error(msg = "Silahkan buat keluarga anda")
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.value = BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah")
            }
        }
    }

    fun createUserRelations(token: String, familyName: String, dataRelationship: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val createRelationsRequest = CreateRelationsRequest(
                    name = familyName,
                    relation_name = dataRelationship
                )

                val response = repository.createUserRelations(token, createRelationsRequest)
                if (response.code() == 200) {
                    _createUserRelations.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    _createUserRelations.postValue(SingleLiveEvent(BaseResponse.Error(msg = response.message())))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.postValue(BaseResponse.Error(msg = e.message + "Sebentar, ada sesuatu yang salah"))
            }
        }
    }

    fun updateRelation(token: String, invitationToken: String, relationName: String) {
        _loading.value = true
        viewModelScope.launch {
            try {

                val updateRelationRequest = UpdateRelationRequest (
                    relation_name = relationName
                )

                val response = repository.updateRelation(token = token, invitationToken = invitationToken, updateRelationRequest)
                if (response.code() == 200) {
                    _updateRelations.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _updateRelations.postValue(BaseResponse.Error(response.message()))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.postValue(BaseResponse.Error(msg = e.message + "Sebentar, ada sesuatu yang salah"))
            }
        }
    }

    fun inviteFamily(token: String, invitationToken: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val inviteFamilyRequest = InviteFamilyRequest(
                    tokenInvitation = invitationToken
                )
                val response = repository.inviteFamily(token = token, inviteFamilyRequest)
                if (response.code() == 200) {
                    _inviteFamily.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                    Log.d("TAG", "inviteFamily: ${response.body()}")
                } else {
                    _inviteFamily.postValue(BaseResponse.Error(response.message()))
                    _loading.value = false
                    Log.d("TAG", "inviteFamilyError: ${response.message()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.postValue(BaseResponse.Error(msg = e.message + "Sebentar, ada sesuatu yang salah"))
            }
        }
    }

    fun createFamilyTree(token: String, createFamilyTreeRequest: CreateFamilyTreeRequest){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.createFamilyTree(token, createFamilyTreeRequest)
                if (response.code() == 200) {
                    _createFamilyTree.value = BaseResponse.Success(response.body())
                    Log.d("family_tree", "success_create_family_tree: ${response.body()}")
                } else {
                    _createFamilyTree.value = BaseResponse.Error(msg = response.message())
                    Log.d("family_tree", "error_create_family_tree: ${response.message()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.value = BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah")
            }
        }
    }

}
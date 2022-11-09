package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.request.UpdateRelationRequest
import com.awp.samakaki.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FamilyTreeViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _findUserRelations = MutableLiveData<BaseResponse<UserRelationsResponse>>()
    val findUserRelations: LiveData<BaseResponse<UserRelationsResponse>> = _findUserRelations

    private val _createUserRelations = MutableLiveData<BaseResponse<CreateRelationsResponse>>()
    val createUserRelations: LiveData<BaseResponse<CreateRelationsResponse>> = _createUserRelations

    private val _updateRelations = MutableLiveData<BaseResponse<UpdateRelationsResponse>>()
    val updateRelations: LiveData<BaseResponse<UpdateRelationsResponse>> = _updateRelations

    private val _createFamilyTree = MutableLiveData<BaseResponse<CreateFamilyTreeResponse>>()
    val createFamilyTree: LiveData<BaseResponse<CreateFamilyTreeResponse>> = _createFamilyTree

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun findUserRelations(token: String){
        viewModelScope.launch {
            try {
                val response = repository.findUserRelations(token)
                if (response.code() == 200){
                    _findUserRelations.value = BaseResponse.Success(response.body())
                    Log.d("find_relations", "success_find_user_relation: ${response.body()}")
                } else {
                    _findUserRelations.value = BaseResponse.Error(msg = "Silahkan buat keluarga anda")
                    Log.d("find_relations", "error_find_user_relation: ${response.message()}")
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
        viewModelScope.launch {
            try {
                val createRelationsRequest = CreateRelationsRequest(
                    name = familyName,
                    relation_name = dataRelationship
                )

                val response = repository.createUserRelations(token, createRelationsRequest)
                if (response.code() == 200) {
                    _createUserRelations.postValue(BaseResponse.Success(response.body()))
                    Log.d("user_relations", "success_create_user_relation: ${response.body()}")
                } else {
                    _createUserRelations.postValue(BaseResponse.Error(msg = response.message()))
                    Log.d("user_relations", "failure_create_user_relation: ${response.message()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
                Log.d("user_relations", "failure_user_relation: ${e.message}")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.postValue(BaseResponse.Error(msg = e.message + "Sebentar, ada sesuatu yang salah"))
                Log.d("user_relations", "failure_user_relation: ${e.message}")
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
                    Log.d("update_relations", "success_update_user_relation: ${response.body()}")
                } else {
                    _updateRelations.postValue(BaseResponse.Error(response.message()))
                    Log.d("update_relations", "failure_update_user_relation: ${response.message()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
                Log.d("user_relations", "failure_user_relation: ${e.message}")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUserRelations.postValue(BaseResponse.Error(msg = e.message + "Sebentar, ada sesuatu yang salah"))
                Log.d("user_relations", "failure_user_relation: ${e.message}")
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
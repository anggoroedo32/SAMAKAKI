package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.CreateFamilyTreeRequest
import com.awp.samakaki.request.CreateRelationsRequest
import com.awp.samakaki.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FamilyTreeViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _findUserRelations = MutableLiveData<BaseResponse<GetUserRelationResponse>>()
    val findUserRelations: LiveData<BaseResponse<GetUserRelationResponse>> = _findUserRelations

    private val _createUserRelations = MutableLiveData<BaseResponse<CreateUserRelationsResponse>>()
    val createUserRelations: LiveData<BaseResponse<CreateUserRelationsResponse>> = _createUserRelations

    private val _createFamilyTree = MutableLiveData<BaseResponse<CreateFamilyTreeResponse>>()
    val createFamilyTree: LiveData<BaseResponse<CreateFamilyTreeResponse>> = _createFamilyTree

    fun findUserRelations(token: String){
        viewModelScope.launch {
            try {
                val response = repository.findUserRelations(token)
                if (response.code() == 200){
                    _findUserRelations.value = BaseResponse.Success(response.body())
                } else {
                    _findUserRelations.value = BaseResponse.Error(msg = "Silahkan buat keluarga anda")
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

    fun createUserRelations(token: String, createRelationsRequest: CreateRelationsRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createUserRelations(token, createRelationsRequest)
                if (response.code() == 200) {
                    _createUserRelations.value = BaseResponse.Success(response.body())
                    Log.d("user_relations", "success_create_user_relation: ${response.body()}")
                } else {
                    _createUserRelations.value = BaseResponse.Error(msg = response.message())
                    Log.d("user_relations", "failure_create_user_relation: ${response.message()}")
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

    fun createFamilyTree(token: String, createFamilyTreeRequest: CreateFamilyTreeRequest){
        viewModelScope.launch {
            try {
                val response = repository.createFamilyTree(token, createFamilyTreeRequest)
                if (response.code() == 200) {
                    _createFamilyTree.value = BaseResponse.Success(response.body())
                    Log.d("family_tree", "success_create_family_tree: ${response.body()}")
                } else {
                    _createFamilyTree.value = BaseResponse.Error(msg = response.message())
                    Log.d("family_tree", "success_create_family_tree: ${response.message()}")
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
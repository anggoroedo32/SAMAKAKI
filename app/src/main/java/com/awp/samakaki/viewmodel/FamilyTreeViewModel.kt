package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.RegisterResponse
import com.awp.samakaki.response.UserRelationsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FamilyTreeViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _userRelations = MutableLiveData<BaseResponse<UserRelationsResponse>>()
    val userRelations: LiveData<BaseResponse<UserRelationsResponse>> = _userRelations

    fun userRelations(token: String){
        viewModelScope.launch {
            try {
                val response = repository.userRelations(token)
                if (response.code() == 200){
                    _userRelations.value = BaseResponse.Success(response.body())
                    Log.d("user_relations", "success_get_user_relations: ${response.body()}")
                } else {
                    _userRelations.value = BaseResponse.Error(msg = "Sebentar, sedang ada masalah")
                    Log.d("login", "failure_login: ${response.errorBody()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _userRelations.value = BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah")
            }
        }
    }

}
package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.UserRequest
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.FindUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){
    private val _findUser = MutableLiveData<BaseResponse<FindUserResponse>>()
    val findUser: LiveData<BaseResponse<FindUserResponse>> = _findUser

    fun findUser(token: String, id: String){
        viewModelScope.launch {
            try {
                val response = repository.findUser(token, id)
                if (response.code() == 200){
                    _findUser.postValue(BaseResponse.Success(response.body()))
                    Log.d("edit_profile", "success_get_error_edit: ${response.body()}")
                } else {
                    _findUser.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada kendala"))
                    Log.d("edit_profile", "isi_error_edit: ${response.errorBody()}")
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _findUser.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada masalah"))
            }
        }
    }

}
package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.NotificationsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _getNotifications = MutableLiveData<BaseResponse<NotificationsResponse>>()
    val getNotifications: LiveData<BaseResponse<NotificationsResponse>> = _getNotifications

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getNotifications(token: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                Log.d("isi_token_repo", token).toString()
                val response = repository.getNotificationByUser(token)
                if (response.code() == 200) {
                    _getNotifications.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _getNotifications.postValue(BaseResponse.Error("Notifications "+ response.message()))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _getNotifications.postValue(BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah"))
            }
        }
    }


}
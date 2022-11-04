package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.NotificationsResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _getNotifications = MutableLiveData<BaseResponse<NotificationsResponse>>()
    val getNotifications: LiveData<BaseResponse<NotificationsResponse>> = _getNotifications


    fun getNotifications(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getNotificationByUser(token)
                if (response.code() == 200) {
                    _getNotifications.postValue(BaseResponse.Success(response.body()))
                    Log.d("get_notif", "success_get_notif: ${response.body()}")
                } else {
                    _getNotifications.postValue(BaseResponse.Error(response.message()))
                    Log.d("get_notif", "failure_get_notif: ${response.message()}")
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
package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.helper.SingleLiveEvent
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.response.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<SingleLiveEvent<BaseResponse<RegisterResponse>>>()
    val registerResponse: LiveData<SingleLiveEvent<BaseResponse<RegisterResponse>>> = _registerResponse

    private val _loginResponse = MutableLiveData<SingleLiveEvent<BaseResponse<LoginResponse>>>()
    val loginResponse: LiveData<SingleLiveEvent<BaseResponse<LoginResponse>>> = _loginResponse


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    email = email,
                    password = password
                )

                val response = repository.login(loginRequest = loginRequest)
                if (response.code() == 200) {
                    _loginResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
//                    Log.d("login", "success_login: ${response.body()}")
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageLoginResponse>() {}.type
                    var errorResponse: MessageLoginResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _loginResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message)))
//                    Log.d("login", "failure_login: ${errorResponse.message.toString()}")
                }

            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _registerResponse.postValue(SingleLiveEvent(BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah")))
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            try {

                val registerRequest = RegisterRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password
                )

                val response = repository.register(registerRequest = registerRequest)
                if (response.code() == 200) {
                    _registerResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
//                    Log.d("register", "success_register: ${response.body()}")
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageResponse>() {}.type
                    var errorResponse: MessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _registerResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message.toString())))
//                    Log.d("register", "failure_register: ${BaseResponse.Error(errorResponse.message.toString())}")
                }

            } catch (e: HttpException) {
              BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
              BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _registerResponse.postValue(SingleLiveEvent(BaseResponse.Error(msg = "Data bermasalah")))
            }
        }
    }


}
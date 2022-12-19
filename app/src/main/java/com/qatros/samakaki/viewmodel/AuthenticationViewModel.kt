package com.qatros.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qatros.samakaki.helper.SingleLiveEvent
import com.qatros.samakaki.repository.RemoteRepository
import com.qatros.samakaki.request.*
import com.qatros.samakaki.response.*
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

    private val _forgotTokenResponse = MutableLiveData<SingleLiveEvent<BaseResponse<ForgotTokenResponse>>>()
    val forgotTokenResponse: LiveData<SingleLiveEvent<BaseResponse<ForgotTokenResponse>>> = _forgotTokenResponse

    private val _resetPasswordResponse = MutableLiveData<SingleLiveEvent<BaseResponse<ResetPasswordResponse>>>()
    val resetPasswordResponse: LiveData<SingleLiveEvent<BaseResponse<ResetPasswordResponse>>> = _resetPasswordResponse

    private val _registerWithTokenResponse = MutableLiveData<SingleLiveEvent<BaseResponse<RegisterWithInvitationResponse>>>()
    val registerWithTokenResponse: LiveData<SingleLiveEvent<BaseResponse<RegisterWithInvitationResponse>>> = _registerWithTokenResponse

    private val _resendResponse = MutableLiveData<BaseResponse<ResendEmailResponse>>()
    val resendResponse: LiveData<BaseResponse<ResendEmailResponse>> = _resendResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun login(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    email = email,
                    password = password
                )

                val response = repository.login(loginRequest = loginRequest)
                if (response.code() == 200) {
                    _loginResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageLoginResponse>() {}.type
                    var errorResponse: MessageLoginResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _loginResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message)))
                    _loading.value = false
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

    fun resendEmailConfirmation(token: String) {
        _loading.value = true
        viewModelScope.launch {
            try {

                val response = repository.resendEmailConfirmation(token)
                if (response.code() == 200) {
                    _resendResponse.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _resendResponse.postValue(BaseResponse.Error(response.message()))
                    _loading.value = false
                }

            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _resendResponse.postValue(BaseResponse.Error(msg = "Sebentar, ada sesuatu yang salah"))
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String) {
        _loading.value = true
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
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageResponse>() {}.type
                    var errorResponse: MessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _registerResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message.toString())))
                    _loading.value = false
                }

            } catch (e: HttpException) {
              BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
              BaseResponse.Error("Cek kembali koneksi internet aynda")
            } catch (e: Exception) {
                _registerResponse.postValue(SingleLiveEvent(BaseResponse.Error(msg = "Data bermasalah")))
            }
        }
    }

    fun registerWithToken(name: String, email: String, phone: String, password: String, token: String) {
        _loading.value = true
        viewModelScope.launch {
            try {

                val registerWithTokenRequest = RegisterWithTokenRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    token = token
                )

                val response = repository.registerWithToken(registerWithTokenRequest = registerWithTokenRequest)
                if (response.code() == 200) {
                    _registerWithTokenResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageResponse>() {}.type
                    var errorResponse: MessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _registerWithTokenResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message.toString())))
                    _loading.value = false
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


    fun forgotToken(email: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val forgotTokenRequest = ForgotTokenRequest(email = email)

                val response = repository.forgotToken(forgotTokenRequest)
                if (response.code() == 200) {
                    _forgotTokenResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageForgotPasswordResponse>() {}.type
                    var errorResponse: MessageForgotPasswordResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _forgotTokenResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message.toString())))
                    Log.d("TAG", "forgotToken: ${errorResponse.message.toString()}")
                    _loading.value = false
                }
            } catch (e: Exception){
                _forgotTokenResponse.postValue(SingleLiveEvent(BaseResponse.Error(e.message)))
            }
        }
    }

    fun resetPassword(email: String, password: String, token: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val resetPasswordRequest = ResetPasswordRequest(
                    email = email,
                    password = password,
                    token = token
                )

                val response = repository.resetPassword(resetPasswordRequest)
                if (response.code() == 200) {
                    _resetPasswordResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<MessageResetPasswordResponse>() {}.type
                    var errorResponse: MessageResetPasswordResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _resetPasswordResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message.toString())))
                    _loading.value = false
                }
            } catch (e: Exception){
                _resetPasswordResponse.postValue(SingleLiveEvent(BaseResponse.Error(e.message)))
                _loading.value = false
            }
        }
    }


}
package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.ForgotTokenRequest
import com.awp.samakaki.request.LoginRequest
import com.awp.samakaki.request.RegisterRequest
import com.awp.samakaki.request.ResetPasswordRequest
import com.awp.samakaki.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _registerResponse = MutableLiveData<BaseResponse<RegisterResponse>>()
    val registerResponse: LiveData<BaseResponse<RegisterResponse>> = _registerResponse

    private val _loginResponse = MutableLiveData<BaseResponse<LoginResponse>>()
    val loginResponse: LiveData<BaseResponse<LoginResponse>> = _loginResponse

    private val _forgotTokenResponse = MutableLiveData<BaseResponse<ForgotTokenResponse>>()
    val forgotTokenResponse: LiveData<BaseResponse<ForgotTokenResponse>> = _forgotTokenResponse

    private val _resetPasswordResponse = MutableLiveData<BaseResponse<ResetPasswordResponse>>()
    val resetPasswordResponse: LiveData<BaseResponse<ResetPasswordResponse>> = _resetPasswordResponse

//    fun register(name: String, email: String, phone: String, password: String){
//        viewModelScope.launch {
//            repository.register(name, email, phone, password).let {
//                if (it.isSuccessful) {
//                    Log.d("register_success", it.body().toString())
//                    _registerResponse.postValue(it.body())
//                }else {
//                    Log.d("register_fail", it.body().toString())
//                }
//            }
//        }
//    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    email = email,
                    password = password
                )

                val response = repository.login(loginRequest = loginRequest)
                if (response.code() == 200) {
                    _loginResponse.value = BaseResponse.Success(response.body())
                    Log.d("login", "success_login: ${response.body()}")
                } else {
                    _loginResponse.value = BaseResponse.Error(response.message().toString())
                    Log.d("login", "failure_login: ${BaseResponse.Error(response.message())}")
                }
            } catch (e: Exception){
                _loginResponse.value = BaseResponse.Error(e.message)
                Log.d("login", "err_login: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String){
        viewModelScope.launch {
            try {

                val registerRequest = RegisterRequest(
                    name = name,
                    email = email,
                    phone = phone,
                    password = password
                )

                val response = repository.register(registerRequest)
                if(response.code() == 200) {
                    _registerResponse.value = BaseResponse.Success(response.body())
                    Log.d("register", "success_register: ${response.body()}")
                } else {
                    _registerResponse.value = BaseResponse.Error(response.message().toString())
                    Log.d("register", "failure_register: ${BaseResponse.Error(response.message())}")
                }
            } catch (e: Exception){
                _registerResponse.value = BaseResponse.Error(e.message)
                Log.d("register", "err_register: ${e.message}")
            }
        }
    }

    fun forgotToken(email: String){
        viewModelScope.launch {
            try {
                val forgotTokenRequest = ForgotTokenRequest(email = email)

                val response = repository.forgotToken(forgotTokenRequest)
                if (response.code() == 200) {
                    _forgotTokenResponse.value = BaseResponse.Success(response.body())
                    Log.d("forgot token", "success_forgot_token: ${response.body()}")
                } else {
                    _forgotTokenResponse.value = BaseResponse.Error(response.message().toString())
                    Log.d("forgot token", "failure_forgot_token: ${BaseResponse.Error(response.message())}")
                }
            } catch (e: Exception){
                _forgotTokenResponse.value = BaseResponse.Error(e.message)
                Log.d("forgot token", "err_forgot_token: ${e.message}")
            }
        }
    }

    fun resetPassword(email: String, password: String, token: String){
        viewModelScope.launch {
            try {
                val resetPasswordRequest = ResetPasswordRequest(
                    email = email,
                    password = password,
                    token = token
                )

                val response = repository.resetPassword(resetPasswordRequest)
                if (response.code() == 200) {
                    _resetPasswordResponse.value = BaseResponse.Success(response.body())
                    Log.d("reset password", "success_reset_password: ${response.body()}")
                } else {
                    _resetPasswordResponse.value = BaseResponse.Error(response.message().toString())
                    Log.d("reset password", "failure_reset_password: ${BaseResponse.Error(response.message())}")
                }
            } catch (e: Exception){
                _resetPasswordResponse.value = BaseResponse.Error(e.message)
                Log.d("reset password", "err_reset_password: ${e.message}")
            }
        }
    }


}
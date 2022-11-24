package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.EditProfileResponse
import com.awp.samakaki.response.FindUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _findUser = MutableLiveData<BaseResponse<FindUserResponse>>()
    val findUser: LiveData<BaseResponse<FindUserResponse>> = _findUser

    private val _editProfile = MutableLiveData<BaseResponse<EditProfileResponse>>()
    val editProfile: LiveData<BaseResponse<EditProfileResponse>> = _editProfile

    private val _editPrivacy = MutableLiveData<BaseResponse<EditProfileResponse>>()
    val editPrivacy: LiveData<BaseResponse<EditProfileResponse>> = _editPrivacy

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun findUser(token: String, id: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.findUser(token, id)
                if (response.code() == 200){
                    _findUser.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _findUser.postValue(BaseResponse.Error(msg = "Silahkan mengisi biodata anda"))
                    _loading.value = false
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


    fun editProfile(
        token: String,
        id: Int?,
        name: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        address: RequestBody,
        dob: RequestBody,
        marriageStatus: RequestBody,
        status: RequestBody,
        avatar: MultipartBody.Part?
    ){
        _loading.value = true
        viewModelScope.launch {
            Log.e("TAG", "editProfile: $name")
            try {
                val response = repository.editProfile(
                    token = token,
                    id = id!!,
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    dob = dob,
                    marriage_status = marriageStatus,
                    status = status,
                    file = avatar
                )
                Log.e("TAG", "editProfile: $dob", )
                if (response.code() == 200) {
                    _editProfile.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _editProfile.postValue(BaseResponse.Error("Error Edit Profile"))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            }
//            catch (e: Exception) {
//                _findUser.postValue(BaseResponse.Error(msg = e.message))
//                Log.d("edit_profile", "got_exception: ${BaseResponse.Error(e.toString() )}")
//            }
        }
    }

    fun editPrivacy(
        token: String,
        id: Int?,
        status: RequestBody
    ){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.editPrivacy(
                    token = token,
                    id = id!!,
                    status = status
                )
                if (response.code() == 200) {
                    _editPrivacy.postValue(BaseResponse.Success(response.body()))
                    _loading.value = false
                } else {
                    _editPrivacy.postValue(BaseResponse.Error("Erorr Edit Privacy"))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            }
        }
    }

}
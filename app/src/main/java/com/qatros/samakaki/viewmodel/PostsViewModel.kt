package com.qatros.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qatros.samakaki.helper.SingleLiveEvent
import com.qatros.samakaki.repository.RemoteRepository
import com.qatros.samakaki.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _listAllPosts = MutableLiveData<BaseResponse<NewPostsResponse>>()
    val listAllPosts: LiveData<BaseResponse<NewPostsResponse>> = _listAllPosts

    private val _listAllPostsByUser = MutableLiveData<SingleLiveEvent<BaseResponse<PostUserResponse>>>()
    val listAllPostsByUser: LiveData<SingleLiveEvent<BaseResponse<PostUserResponse>>> = _listAllPostsByUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _createPostResponse = MutableLiveData<BaseResponse<PostsResponse>>()
    val createPostResponse: LiveData<BaseResponse<PostsResponse>> = _createPostResponse

    private val _deletePostResponse = MutableLiveData<SingleLiveEvent<BaseResponse<DeletePostResponse>>>()
    val deletePostResponse: LiveData<SingleLiveEvent<BaseResponse<DeletePostResponse>>> = _deletePostResponse

    fun getAllPostsByFamily(token: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getAllPostsByFamily(token = token)
                if(response.code() == 200) {
                    _listAllPosts.value = BaseResponse.Success(response.body())
                    Log.d("TAG", "getAllPostsByFamily: ${response.body()}")
                    _loading.value = false
                } else {
                    _listAllPosts.value = BaseResponse.Error("Erorr Get Posts")
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _listAllPosts.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada masalah"))
            }
        }
    }

    fun getAllPostsByUser(token: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getAllPostsByUser(token = token)
                if(response.code() == 200) {
                    _listAllPostsByUser.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<SingleMessageResponse>() {}.type
                    var errorResponse: SingleMessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _listAllPostsByUser.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message)))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _listAllPostsByUser.postValue(SingleLiveEvent(BaseResponse.Error(msg = "Sebentar, sedang ada masalah")))
            }
        }
    }

    fun createPosts(
        token: String, descriptions: RequestBody, status: RequestBody, content: MultipartBody.Part?){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.createPosts(
                    token = token,
                    descriptions = descriptions,
                    status = status,
                    content = content)
                if(response.code() == 200) {
                    _createPostResponse.value = BaseResponse.Success(response.body())
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<SingleMessageResponse>() {}.type
                    var errorResponse: SingleMessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _createPostResponse.postValue(BaseResponse.Error(errorResponse.message))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _createPostResponse.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada masalah"))
            }
        }
    }

    fun deletePost(token: String, id: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.deletePost(token = token, id = id)
                if (response.code() == 200) {
                    _deletePostResponse.postValue(SingleLiveEvent(BaseResponse.Success(response.body())))
                    _loading.value = false
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<SingleMessageResponse>() {}.type
                    var errorResponse: SingleMessageResponse = gson.fromJson(response.errorBody()?.string(), type)
                    _deletePostResponse.postValue(SingleLiveEvent(BaseResponse.Error(errorResponse.message)))
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Coba kembali nanti, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _deletePostResponse.postValue(SingleLiveEvent(BaseResponse.Error(msg = "Coba kembali nanti, sedang ada masalah")))
            }
        }
    }

}
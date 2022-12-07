package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.PostRequest
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.NewPostsResponse
import com.awp.samakaki.response.PostUserResponse
import com.awp.samakaki.response.PostsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _listAllPosts = MutableLiveData<BaseResponse<NewPostsResponse>>()
    val listAllPosts: LiveData<BaseResponse<NewPostsResponse>> = _listAllPosts

    private val _listAllPostsByUser = MutableLiveData<BaseResponse<PostUserResponse>>()
    val listAllPostsByUser: LiveData<BaseResponse<PostUserResponse>> = _listAllPostsByUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _createPostResponse = MutableLiveData<BaseResponse<PostsResponse>>()
    val createPostResponse: LiveData<BaseResponse<PostsResponse>> = _createPostResponse

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
                    _listAllPostsByUser.value = BaseResponse.Success(response.body())
                    _loading.value = false
                } else {
                    _listAllPostsByUser.value = BaseResponse.Error("Erorr Get Posts")
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _listAllPostsByUser.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada masalah"))
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
                    _createPostResponse.value = BaseResponse.Error(response.message())
                    Log.d("TAG", "createPosts: ${response.code()}")
                    _loading.value = false
                }
            } catch (e: HttpException) {
                BaseResponse.Error(msg = e.message() + "Sebentar, sedang ada masalah")
            } catch (e: IOException) {
                BaseResponse.Error("Cek kembali koneksi internet anda")
            } catch (e: Exception) {
                _listAllPostsByUser.postValue(BaseResponse.Error(msg = "Sebentar, sedang ada masalah"))
            }
        }
    }
}
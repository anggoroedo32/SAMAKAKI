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
import com.awp.samakaki.response.PostsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _listAllPosts = MutableLiveData<BaseResponse<NewPostsResponse>>()
    val listAllPosts: LiveData<BaseResponse<NewPostsResponse>> = _listAllPosts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _createPostResponse = MutableLiveData<BaseResponse<PostsResponse>>()
    val createPostResponse: LiveData<BaseResponse<PostsResponse>> = _createPostResponse

    fun getAllPostsByFamily(token: String){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                val response = repository.getAllPostsByFamily(token = token)
                if(response.code() == 200) {
                    _listAllPosts.value = BaseResponse.Success(response.body())
                    Log.d("get_posts", "success_creating: ${response.body()}")
                } else {
                    _listAllPosts.value = BaseResponse.Error("Erorr Get Posts")
                    Log.d("get_posts", "failure_creatuing: ${BaseResponse.Error(response.message())}")
                }
            }
        }
    }

    fun getAllPostsByUser(token: String){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                val response = repository.getAllPostsByUser(token = token)
                if(response.code() == 200) {
                    _listAllPosts.value = BaseResponse.Success(response.body())
                    Log.d("get_posts", "success_creating: ${response.body()}")
                } else {
                    _listAllPosts.value = BaseResponse.Error("Erorr Get Posts")
                    Log.d("get_posts", "failure_creatuing: ${BaseResponse.Error(response.message())}")
                }
            }
        }
    }

    fun createPosts(
        token: String, descriptions: RequestBody, status: RequestBody, content: MultipartBody.Part?){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                val response = repository.createPosts(
                        token = token,
                        descriptions = descriptions,
                        status = status,
                        content = content)
                if(response.code() == 200) {
                    _createPostResponse.value = BaseResponse.Success(response.body())
                    _loading.value = false
                } else {
                    _createPostResponse.value = BaseResponse.Error("Erorr Create Posts")
                    _loading.value = false
                }
            }
        }
    }
}
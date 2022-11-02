package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.PostRequest
import com.awp.samakaki.response.BaseResponse
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

    private val _listAllPosts = MutableLiveData<PostsResponse>()
    val listAllPosts: LiveData<PostsResponse> = _listAllPosts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val isCreate = MutableLiveData<BaseResponse<PostsResponse>>()
    val observeIsCreate: LiveData<BaseResponse<PostsResponse>> = isCreate

    fun getAllPosts(){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                repository.getAllPosts().let {
                    Log.d("masukkk", "getAllPosts: ${it.body()}")
                    if (it.isSuccessful){
                        _loading.value = false
                        _listAllPosts.postValue(it.body())
                    } else {
                        Log.e("error", "getAllPosts: error")
                    }
                }
            }
        }
    }

    fun createPosts(
        token: String, descriptions: RequestBody, status: RequestBody, content: MultipartBody.Part){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                try {
                    val response = repository.createPosts(token, descriptions, status, content)
                } catch (e: Throwable) {
                    BaseResponse.Error(e.toString())
                }
            }
        }
    }
}
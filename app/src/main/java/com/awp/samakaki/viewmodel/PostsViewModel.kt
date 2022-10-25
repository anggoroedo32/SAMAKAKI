package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.Data
import com.awp.samakaki.response.PostItem
import com.awp.samakaki.response.PostsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun createPosts(postItem: String){
        viewModelScope.launch {
            _loading.value = true
            viewModelScope.launch {
                try {
                    val response = repository.createPosts(postItem)
                    repository.createPosts(postItem)
                    isCreate.value = BaseResponse.Success(response.body())
                } catch (e: Throwable) {
                    BaseResponse.Error(e.toString())
                }
            }
        }
    }
}
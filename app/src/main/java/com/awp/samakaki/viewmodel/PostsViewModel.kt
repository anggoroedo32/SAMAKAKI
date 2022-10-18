package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.response.Data
import com.awp.samakaki.response.PostsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: RemoteRepository) : ViewModel(){

    private val _listAllPosts = MutableLiveData<PostsResponse>()
    val listAllPosts: LiveData<PostsResponse> = _listAllPosts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

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

}
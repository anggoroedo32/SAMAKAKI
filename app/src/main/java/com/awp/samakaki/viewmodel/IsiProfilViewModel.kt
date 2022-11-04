package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.BiodataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class IsiProfilViewModel @Inject constructor(private val repository: RemoteRepository): ViewModel() {

    private val _createBiodataResponse = MutableLiveData<BaseResponse<BiodataResponse>>()
    val createBiodataResponse: LiveData<BaseResponse<BiodataResponse>> = _createBiodataResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun createBiodata(
        token: String,
        address: RequestBody,
        dob: RequestBody,
        marriageStatus: RequestBody,
        status: RequestBody,
        avatar: MultipartBody.Part
    ){
        _loading.value = true
        viewModelScope.launch {
                val response = repository.createBiodata(
                    token = token,
                    address = address,
                    dob = dob,
                    marriage_status = marriageStatus,
                    status = status,
                    file = avatar
                )

                if(response.code() == 200) {
                    _createBiodataResponse.value = BaseResponse.Success(response.body())
                    Log.d("data_biodata", "success_creating: ${response.body()}")
                } else {
                    _createBiodataResponse.value = BaseResponse.Error("Erorr Create Biodata")
                    Log.d("data_biodata", "failure_creatuing: ${BaseResponse.Error(response.message())}")
                }
            }
        }
    }

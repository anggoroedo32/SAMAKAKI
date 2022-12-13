package com.qatros.samakaki.viewmodel

import androidx.lifecycle.*
import com.qatros.samakaki.repository.RemoteRepository
import com.qatros.samakaki.response.BaseResponse
import com.qatros.samakaki.response.BiodataResponse
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
                    _loading.value = false
                } else {
                    _createBiodataResponse.value = BaseResponse.Error("Erorr Create Biodata")
                    _loading.value = false
                }
            }
        }
    }

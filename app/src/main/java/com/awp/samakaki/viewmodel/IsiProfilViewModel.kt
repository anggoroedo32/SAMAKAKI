package com.awp.samakaki.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.awp.samakaki.repository.RemoteRepository
import com.awp.samakaki.request.BiodataRequest
import com.awp.samakaki.response.BaseResponse
import com.awp.samakaki.response.BiodataResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class IsiProfilViewModel @Inject constructor(private val repository: RemoteRepository): ViewModel() {
    private val _createBiodataResponse = MutableLiveData<BiodataResponse>()
    val createBiodataResponse: LiveData<BiodataResponse> = _createBiodataResponse

    fun createBiodata(token: String, address: String, dob: String, marriageStatus: String, status: String){
        viewModelScope.launch {
            try {
                val biodataRequest = BiodataRequest(
                    address = address,
                    dob = dob,
                    marriageStatus = marriageStatus,
                    status = status
                )
                val response = repository.createBiodata(token, biodataRequest)
                if(response.code() == 200) {
                    _createBiodataResponse.value = response.body()
                    Log.d("data_biodata", "success_creating: ${response.body()}")
                } else {
                    _createBiodataResponse.value = response.body()
                    Log.d("data_biodata", "failure_creatuing: ${BaseResponse.Error(response.message())}")
                }
            } catch (e: Exception){

            }
        }
    }
}
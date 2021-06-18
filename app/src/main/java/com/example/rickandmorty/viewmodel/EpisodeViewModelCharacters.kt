package com.example.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.api.ApiUtils
import com.example.rickandmorty.model.CertainEpisodeModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EpisodeViewModelCharacters : ViewModel() {


    fun getEpisodeById(id : Int) : MutableLiveData<CertainEpisodeModel>? {
        val data = MutableLiveData<CertainEpisodeModel>()
        viewModelScope.launch {
            ApiUtils.apiService?.getEpisodeById(id)?.enqueue(object  : Callback<CertainEpisodeModel>{
                override fun onResponse(
                    call: Call<CertainEpisodeModel>,
                    response: Response<CertainEpisodeModel>
                ) {
                    if(response.isSuccessful && response.body() != null){
                        data?.value = response.body()
                    }
                }

                override fun onFailure(call: Call<CertainEpisodeModel>, t: Throwable) {
                }

            })
        }
        return data
    }

}
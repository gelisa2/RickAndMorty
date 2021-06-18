package com.example.rickandmorty.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.api.ApiUtils
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersViewModel : ViewModel() {

    fun getCharacterById(id : Int) : MutableLiveData<Character>{
        val data = MutableLiveData<Character>()
        ApiUtils.apiService?.getCharacterById(id)?.enqueue(object  : Callback<Character>{
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if(response.isSuccessful && response.body() != null){
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
            }

        })
        return data
    }

    fun getAllCharacter() : MutableLiveData<List<Character>> {
        val namesList = mutableListOf<Int>()
        val data = MutableLiveData<List<Character>>()
        for (i in  1..671){
            namesList.add(i)
        }

        ApiUtils.apiService?.getAllCharacterById(namesList)?.enqueue(object : Callback<List<Character>>{
            override fun onResponse(
                call: Call<List<Character>>,
                response: Response<List<Character>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {

            }

        })

        return data
    }

    fun getCharacterByName(name : String) : MutableLiveData<CharacterModel> {
        val data = MutableLiveData<CharacterModel>()

        ApiUtils.apiService?.getCharacterByName(name)?.enqueue(object  : Callback<CharacterModel>{
            override fun onResponse(call: Call<CharacterModel>, response: Response<CharacterModel>) {
                if(response.isSuccessful && response.body() != null){
                    data.value = response.body()
                }
            }

            override fun onFailure(call: Call<CharacterModel>, t: Throwable) {

            }

        })
        return data
    }
}












package com.squareit.rickmorty.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.data.repository.CharacterRepository

class CharacterViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _result = MutableLiveData<Resource<List<Character>>>()
    val result: LiveData<Resource<List<Character>>> = _result

    fun getCharacters(shouldFetch: Boolean) =
        repository.getCharacters(shouldFetch) { _result.value = it }

    fun getCharactersFavoriteDB() =
        repository.getCharactersFavoriteDB { _result.value = it }

    fun updateCharacterFavoriteDB(id: Int, isFavorite: Boolean) =
        repository.updateCharactersFavoriteDB(id, isFavorite) { _result.value = it }

    override fun onCleared() {
        super.onCleared()
        repository.onDestroy()
    }
}
package com.squareit.rickmorty.ui.favorite_detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.data.repository.CharacterRepository
import javax.inject.Inject

class CharacterDetailViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _result = MutableLiveData<Resource<Character>>()
    val result: LiveData<Resource<Character>> = _result

    fun getCharacter(id: Int) =
        repository.getCharacterDB(id) { _result.value = it }

    override fun onCleared() {
        super.onCleared()
        repository.onDestroy()
    }
}
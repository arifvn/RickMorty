package com.squareit.rickmorty.data.datasource.remote

import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.data.entities.CharacterList
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {
    @GET("character")
    fun getCharacters(): Flowable<CharacterList>

    @GET("character/{id}")
    fun getCharacter(@Path("id") id: Int): Flowable<Character>
}
package com.squareit.rickmorty.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.squareit.rickmorty.data.entities.Character
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters")
    fun getCharacters(): Flowable<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): Flowable<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<Character>)

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    fun updateCharacterFavorite(id: Int, isFavorite: Boolean): Completable

    @Query("SELECT * FROM characters WHERE isFavorite = 1")
    fun getCharactersFavorite(): Flowable<List<Character>>
}
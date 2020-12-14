package com.squareit.rickmorty.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.squareit.rickmorty.data.entities.Character

@Database(entities = [Character::class], version = 2, exportSchema = false)
abstract class CharacterDB : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: CharacterDB? = null

        fun getDatabase(context: Context): CharacterDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): CharacterDB =
            Room.databaseBuilder(
                context.applicationContext,
                CharacterDB::class.java, "character_database"
            ).fallbackToDestructiveMigration().build()
    }
}
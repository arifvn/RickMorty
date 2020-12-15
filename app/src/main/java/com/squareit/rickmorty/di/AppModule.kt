package com.squareit.rickmorty.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareit.rickmorty.data.datasource.common.ApiConstant
import com.squareit.rickmorty.data.datasource.local.CharacterDB
import com.squareit.rickmorty.data.datasource.local.CharacterDao
import com.squareit.rickmorty.data.datasource.remote.CharacterService
import com.squareit.rickmorty.data.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    // REMOTE SERVICE
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConstant.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()

    // REMOTE SOURCE
    @Provides
    fun provideCharacterService(retrofit: Retrofit): CharacterService =
        retrofit.create(CharacterService::class.java)

    // LOCAL SERVICE
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        CharacterDB.getDatabase(context)

    // LOCAL SOURCE
    @Singleton
    @Provides
    fun provideCharacterDao(db: CharacterDB) = db.characterDao()

    // Repository
    @Provides
    fun provideCharacterRepository(
        remoteDataSource: CharacterService,
        localDataSource: CharacterDao

    ) = CharacterRepository(remoteDataSource, localDataSource)
}
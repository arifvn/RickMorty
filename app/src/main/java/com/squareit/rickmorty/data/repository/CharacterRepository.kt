package com.squareit.rickmorty.data.repository

import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.datasource.common.networkBoundResource
import com.squareit.rickmorty.data.datasource.local.CharacterDao
import com.squareit.rickmorty.data.datasource.remote.CharacterService
import com.squareit.rickmorty.data.entities.Character
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterService,
    private val localDataSource: CharacterDao
) {

    private val disposables = CompositeDisposable()

    fun getCharacters(
        shouldFetch: Boolean,
        callback: (Resource<List<Character>>) -> Unit
    ) {
        val flowable = networkBoundResource(
            shouldFetch = shouldFetch,
            loadFromRemote = { remoteDataSource.getCharacters() },
            saveToLocal = { response -> localDataSource.insertCharacters(response.results) },
            loadFromLocal = { localDataSource.getCharacters() }
        )

        val disposable = flowable
            .doOnSubscribe { callback(Resource.loading(null)) }
            .subscribe(
                { callback(Resource.success(it)) },
                { callback(Resource.error(it.message!!)) }
            )

        disposables.add(disposable)
    }

    fun getCharactersFavoriteDB(callback: (Resource<List<Character>>) -> Unit) {
        val disposable = localDataSource.getCharactersFavorite()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { callback(Resource.loading(null)) }
            .subscribe(
                { callback(Resource.success(it)) },
                { callback(Resource.error(it.localizedMessage!!, null)) }
            )
        disposables.add(disposable)
    }

    fun getCharacterDB(id: Int, callback: (Resource<Character>) -> Unit) {
        val disposable = localDataSource.getCharacter(id)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { callback(Resource.success(it)) },
                { callback(Resource.error(it.localizedMessage!!, null)) }
            )

        disposables.add(disposable)
    }

    fun updateCharactersFavoriteDB(
        id: Int,
        isFavorite: Boolean,
        callback: (Resource<List<Character>>) -> Unit
    ) {
        val disposable = localDataSource.updateCharacterFavorite(id, isFavorite)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { callback(Resource.success(null)) },
                { callback(Resource.error(it.localizedMessage!!, null)) }
            )
        disposables.add(disposable)
    }

    fun onDestroy() = disposables.clear()
}
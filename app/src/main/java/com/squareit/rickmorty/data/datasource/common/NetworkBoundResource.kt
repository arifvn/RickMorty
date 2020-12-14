package com.squareit.rickmorty.data.datasource.common

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

/*
This should be an other way to apply SOT rules Instead of NetworkBound.kt
I used it a lot before I realized to be using inline function
*/

abstract class NetworkBoundResource<LocalType, RemoteType> {

    private var result: Flowable<Resource<LocalType>>

    init {
        val diskObservable = Flowable.defer {
            loadFromDb().subscribeOn(Schedulers.computation())
        }

        val networkObservable = Flowable.defer {
            createCall()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext {
                    try {
                        saveCallResult(it)
                    } catch (e: Throwable) {
                        throw Exception(e.localizedMessage)
                    }
                }
                .flatMap { loadFromDb() }
        }

        if (shouldFetch()) {
            result = networkObservable
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it.localizedMessage!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { Resource.loading(null) }
        } else {
            result = diskObservable
                .map { Resource.success(it) }
                .onErrorReturn { Resource.error(it.localizedMessage!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { Resource.loading(null) }
        }
    }

    fun getAsFlowable(): Flowable<Resource<LocalType>> = result

    protected abstract fun shouldFetch(): Boolean

    protected abstract fun createCall(): Flowable<RemoteType>

    protected abstract fun saveCallResult(item: RemoteType)

    protected abstract fun loadFromDb(): Flowable<LocalType>
}
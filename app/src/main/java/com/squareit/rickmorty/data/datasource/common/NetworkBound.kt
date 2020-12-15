package com.squareit.rickmorty.data.datasource.common

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

inline fun <LocalType, RemoteType> networkBoundResource(
    shouldFetch: Boolean,
    crossinline loadFromRemote: () -> Flowable<RemoteType>,
    crossinline saveToLocal: (response: RemoteType) -> Unit,
    crossinline loadFromLocal: () -> Flowable<LocalType>,
): Flowable<LocalType> {
    return if (shouldFetch) {
        loadFromRemote()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { saveToLocal(it) }
            .flatMap { loadFromLocal() }
            .observeOn(AndroidSchedulers.mainThread())
    } else {
        loadFromLocal()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
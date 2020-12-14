package com.squareit.rickmorty.data.datasource.common

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?, message: String? = null): Resource<T> =
            Resource(Status.SUCCESS, data, message)

        fun <T> error(message: String, data: T? = null): Resource<T> =
            Resource(Status.ERROR, data, message)

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(Status.LOADING, data, null)
    }
}
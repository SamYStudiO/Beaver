package net.samystudio.beaver.data.remote

/**
 * Async request states, response contains data of type [T].
 */
sealed class DataRequestState<T> {
    class Start<T> : DataRequestState<T>()
    class Success<T>(var data: T) : DataRequestState<T>()
    class Error<T>(val error: Throwable) : DataRequestState<T>()
}
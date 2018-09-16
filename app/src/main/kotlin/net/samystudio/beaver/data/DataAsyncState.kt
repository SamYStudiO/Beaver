package net.samystudio.beaver.data

/**
 * Async request states, response contains data of type [T].
 */
sealed class DataAsyncState<T> {
    class Started<T> : DataAsyncState<T>()
    class Completed<T>(var data: T) : DataAsyncState<T>()
    class Error<T>(val error: Throwable) : DataAsyncState<T>()
}
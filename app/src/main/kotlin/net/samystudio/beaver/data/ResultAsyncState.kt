package net.samystudio.beaver.data

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    class Started<T> : ResultAsyncState<T>()
    class Completed<T>(var data: T) : ResultAsyncState<T>()
    class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
}
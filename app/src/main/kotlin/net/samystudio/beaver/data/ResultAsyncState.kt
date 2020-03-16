package net.samystudio.beaver.data

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    class Started<T> : ResultAsyncState<T>()
    class Completed<T>(var data: T) : ResultAsyncState<T>()
    class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
    class Canceled<T> : ResultAsyncState<T>()
    class Terminate<T> : ResultAsyncState<T>()
}

fun <T> Single<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    toObservable().toResultAsyncState()

fun <T> Observable<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    compose(resultAsyncStateTransformer())

private fun <T> resultAsyncStateTransformer(): ObservableTransformer<T, ResultAsyncState<T>> =
    ObservableTransformer { upstream ->
        upstream.map {
                @Suppress("USELESS_CAST")
                ResultAsyncState.Completed(it) as ResultAsyncState<T>
            }
            .startWith(ResultAsyncState.Started())
            .doOnDispose { ResultAsyncState.Canceled<T>() }
            .onErrorReturn { ResultAsyncState.Failed(it) }
            .doFinally { ResultAsyncState.Terminate<T>() }

    }
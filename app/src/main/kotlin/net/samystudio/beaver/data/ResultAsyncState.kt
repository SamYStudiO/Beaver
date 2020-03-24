package net.samystudio.beaver.data

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    class Started<T> : ResultAsyncState<T>()
    class Completed<T>(var data: T) : ResultAsyncState<T>()
    class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
    class Terminate<T> : ResultAsyncState<T>()
}

fun <T> Single<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    toObservable().toResultAsyncState()

fun <T> Observable<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    compose(resultAsyncStateTransformer())

private fun <T> resultAsyncStateTransformer(): ObservableTransformer<T, ResultAsyncState<T>> =
    ObservableTransformer { upstream ->
        upstream
            .map {
                @Suppress("USELESS_CAST")
                ResultAsyncState.Completed<T>(it) as ResultAsyncState<T>
            }
            .startWith(ResultAsyncState.Started<T>())
            .onErrorReturn { ResultAsyncState.Failed<T>(it) }
            .concatWith(Observable.just(ResultAsyncState.Terminate<T>()))
    }
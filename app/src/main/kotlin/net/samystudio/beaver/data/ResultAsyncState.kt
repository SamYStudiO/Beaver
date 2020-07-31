package net.samystudio.beaver.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Single

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    class Started<T> : ResultAsyncState<T>()
    class Completed<T>(var data: T) : ResultAsyncState<T>()
    class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
}

fun <T> Single<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    toObservable().toResultAsyncState()

fun <T> Observable<T>.toResultAsyncState(): Observable<ResultAsyncState<T>> =
    compose(resultAsyncStateTransformer())

@Suppress("RemoveExplicitTypeArguments")
private fun <T> resultAsyncStateTransformer(): ObservableTransformer<T, ResultAsyncState<T>> =
    ObservableTransformer { upstream ->
        upstream
            .map {
                @Suppress("USELESS_CAST")
                ResultAsyncState.Completed<T>(it) as ResultAsyncState<T>
            }
            .startWithItem(ResultAsyncState.Started<T>())
            .onErrorReturn { ResultAsyncState.Failed<T>(it) }
    }
package net.samystudio.beaver.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Single


/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val error: Throwable) : AsyncState()
}

fun Completable.toAsyncState(): Observable<AsyncState> =
    toSingleDefault(AsyncState.Completed as AsyncState).toObservable().toAsyncState()

fun Single<*>.toAsyncState(): Observable<AsyncState> =
    toObservable().toAsyncState()

fun Observable<*>.toAsyncState(): Observable<AsyncState> =
    compose(asyncStateTransformer())

private fun asyncStateTransformer(): ObservableTransformer<Any, AsyncState> =
    ObservableTransformer { upstream ->
        upstream
            .map {
                @Suppress("USELESS_CAST")
                AsyncState.Completed as AsyncState
            }
            .startWithItem(AsyncState.Started)
            .onErrorReturn { AsyncState.Failed(it) }
    }
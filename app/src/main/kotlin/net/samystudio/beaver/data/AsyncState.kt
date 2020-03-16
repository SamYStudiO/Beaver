package net.samystudio.beaver.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val error: Throwable) : AsyncState()
    object Canceled : AsyncState()
    object Terminate : AsyncState()
}

fun Completable.toAsyncState(): Observable<AsyncState> =
    toSingleDefault(AsyncState.Completed as AsyncState).toObservable().toAsyncState()

fun Single<*>.toAsyncState(): Observable<AsyncState> =
    toObservable().toAsyncState()

fun Observable<*>.toAsyncState(): Observable<AsyncState> =
    compose(asyncStateTransformer())

private fun asyncStateTransformer(): ObservableTransformer<Any, AsyncState> =
    ObservableTransformer { upstream ->
        upstream.map {
                @Suppress("USELESS_CAST")
                AsyncState.Completed as AsyncState
            }
            .startWith(AsyncState.Started)
            .onErrorReturn { AsyncState.Failed(it) }
            .doOnDispose { AsyncState.Canceled }
            .doFinally { AsyncState.Terminate }
    }
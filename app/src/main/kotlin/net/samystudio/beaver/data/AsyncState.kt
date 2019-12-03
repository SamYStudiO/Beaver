package net.samystudio.beaver.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

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

fun Observable<*>.toAsyncState(): Observable<AsyncState> =
    compose(asyncStateTransformer())

private fun asyncStateTransformer(): ObservableTransformer<Any, AsyncState> =
    ObservableTransformer { upstream ->
        upstream.map { AsyncState.Completed as AsyncState }
            .startWith(AsyncState.Started)
            .onErrorReturn { AsyncState.Failed(it) }
    }
@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Single
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showLoaderDialog

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

fun AsyncState.handleStates(
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {},
    complete: () -> Unit = {},
) {
    when (this) {
        is AsyncState.Started -> started()
        is AsyncState.Completed -> complete()
        is AsyncState.Failed -> failed(this.error)
    }
}

fun AsyncState.handleStatesFromFragmentWithLoaderDialog(
    fragment: Fragment,
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = { },
    complete: () -> Unit = { },
) {
    when (this) {
        is AsyncState.Started -> {
            fragment.showLoaderDialog()
            started()
        }
        is AsyncState.Completed -> {
            fragment.hideLoaderDialog()
            complete()
        }
        is AsyncState.Failed -> {
            fragment.hideLoaderDialog()
            failed(this.error)
        }
    }
}

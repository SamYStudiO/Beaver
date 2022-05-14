@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.*
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val throwable: Throwable) : AsyncState()
}

fun Completable.toAsyncState(): Flowable<AsyncState> =
    toSingleDefault(AsyncState.Completed as AsyncState).toFlowable().toAsyncState()

fun Single<*>.toAsyncState(): Flowable<AsyncState> =
    toFlowable().toAsyncState()

fun Observable<*>.toAsyncState(
    strategy: BackpressureStrategy = BackpressureStrategy.LATEST
): Flowable<AsyncState> =
    toFlowable(strategy).toAsyncState()

fun Flowable<*>.toAsyncState(): Flowable<AsyncState> =
    compose(flowableAsyncStateTransformer())

fun AsyncState.handleStates(
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {},
    complete: () -> Unit = {},
) {
    when (this) {
        is AsyncState.Started -> started()
        is AsyncState.Completed -> complete()
        is AsyncState.Failed -> failed(this.throwable)
    }
}

fun AsyncState.handleStatesFromFragmentWithLoaderDialog(
    fragment: Fragment,
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = { fragment.showErrorDialog(throwable = it) },
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
            failed(this.throwable)
        }
    }
}

@Suppress("RemoveExplicitTypeArguments")
private fun <T : Any> flowableAsyncStateTransformer(): FlowableTransformer<T, AsyncState> =
    FlowableTransformer { upstream ->
        upstream
            .map {
                @Suppress("USELESS_CAST")
                AsyncState.Completed as AsyncState
            }
            .startWithItem(AsyncState.Started)
            .onErrorReturn { AsyncState.Failed(it) }
    }

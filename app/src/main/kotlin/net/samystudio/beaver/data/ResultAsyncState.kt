@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.*
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    open class Started<T> : ResultAsyncState<T>()
    open class Completed<T>(var data: T) : ResultAsyncState<T>()
    open class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
}

fun <T : Any> Single<T>.toResultAsyncState(): Flowable<ResultAsyncState<T>> =
    toFlowable().toResultAsyncState()

fun <T : Any> Observable<T>.toResultAsyncState(
    strategy: BackpressureStrategy = BackpressureStrategy.LATEST
): Flowable<ResultAsyncState<T>> =
    toFlowable(strategy).toResultAsyncState()

fun <T : Any> Flowable<T>.toResultAsyncState(): Flowable<ResultAsyncState<T>> =
    compose(flowableResultAsyncStateTransformer())

fun <T : Any> ResultAsyncState<T>.handleStates(
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {},
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is ResultAsyncState.Started -> started()
        is ResultAsyncState.Completed -> complete(this.data)
        is ResultAsyncState.Failed -> failed(this.error)
    }
}

fun <T> ResultAsyncState<T>.handleStatesFromFragmentWithLoaderDialog(
    fragment: Fragment,
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = { fragment.showErrorDialog(throwable = it) },
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is ResultAsyncState.Started -> {
            fragment.showLoaderDialog()
            started()
        }
        is ResultAsyncState.Completed -> {
            fragment.hideLoaderDialog()
            complete(this.data)
        }
        is ResultAsyncState.Failed -> {
            fragment.hideLoaderDialog()
            failed(this.error)
        }
    }
}

@Suppress("RemoveExplicitTypeArguments")
private fun <T : Any> flowableResultAsyncStateTransformer(): FlowableTransformer<T, ResultAsyncState<T>> =
    FlowableTransformer { upstream ->
        upstream
            .map {
                @Suppress("USELESS_CAST")
                ResultAsyncState.Completed<T>(it) as ResultAsyncState<T>
            }
            .startWithItem(ResultAsyncState.Started<T>())
            .onErrorReturn { ResultAsyncState.Failed<T>(it) }
    }

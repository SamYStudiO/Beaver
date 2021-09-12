@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    open class Idle<T> : ResultAsyncState<T>()
    open class Started<T> : ResultAsyncState<T>()
    open class Completed<T>(var data: T) : ResultAsyncState<T>()
    open class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
}

suspend inline fun <T, D> MutableStateFlow<ResultAsyncState<D>>.toAsyncState(
    triggerData: T,
    callee: (T) -> D
) {
    emit(ResultAsyncState.Started())
    try {
        emit(ResultAsyncState.Completed(callee.invoke(triggerData)))
    } catch (e: Throwable) {
        emit(ResultAsyncState.Failed(e))
    } finally {
        emit(ResultAsyncState.Idle())
    }
}

fun <T> ResultAsyncState<T>.handleStates(
    idle: () -> Unit = { },
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {},
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is ResultAsyncState.Idle -> idle()
        is ResultAsyncState.Started -> started()
        is ResultAsyncState.Completed -> complete(this.data)
        is ResultAsyncState.Failed -> failed(this.error)
    }
}

fun <T> ResultAsyncState<T>.handleStatesFromFragmentWithLoaderDialog(
    fragment: Fragment,
    idle: () -> Unit = { },
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = { fragment.showErrorDialog(throwable = it) },
    complete: (result: T) -> Unit = { },
) {
    when (this) {
        is ResultAsyncState.Idle -> idle()
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

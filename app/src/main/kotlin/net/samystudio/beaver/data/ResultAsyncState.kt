@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains data of type [T].
 */
sealed class ResultAsyncState<T> {
    class Started<T> : ResultAsyncState<T>()
    data class Completed<T>(val data: T) : ResultAsyncState<T>()
    data class Failed<T>(val error: Throwable) : ResultAsyncState<T>()
}

fun <T : Any> Flow<T>.toResultAsyncState(): Flow<ResultAsyncState<T>> =
    map {
        @Suppress("USELESS_CAST")
        ResultAsyncState.Completed(it) as ResultAsyncState<T>
    }.catch {
        emit(ResultAsyncState.Failed(it))
    }.onStart {
        emit(ResultAsyncState.Started())
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
    }
}

fun <T> ResultAsyncState<T>.handleStates(
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

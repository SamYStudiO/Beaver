@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showErrorDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    data class Failed(val throwable: Throwable) : AsyncState()
}

fun <T : Any> Flow<T>.toAsyncState(): Flow<AsyncState> =
    map {
        @Suppress("USELESS_CAST")
        AsyncState.Completed as AsyncState
    }.catch {
        emit(AsyncState.Failed(it))
    }.onStart {
        emit(AsyncState.Started)
    }

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

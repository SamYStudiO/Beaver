@file:Suppress("unused")

package net.samystudio.beaver.data

import androidx.fragment.app.Fragment
import net.samystudio.beaver.util.hideLoaderDialog
import net.samystudio.beaver.util.showLoaderDialog

/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Idle : AsyncState()
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val error: Throwable) : AsyncState()
}

fun AsyncState.handleStates(
    idle: () -> Unit = { },
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = {},
    complete: () -> Unit = {},
) {
    when (this) {
        is AsyncState.Idle -> idle()
        is AsyncState.Started -> started()
        is AsyncState.Completed -> complete()
        is AsyncState.Failed -> failed(this.error)
    }
}

fun AsyncState.handleStatesFromFragmentWithLoaderDialog(
    fragment: Fragment,
    idle: () -> Unit = { },
    started: () -> Unit = { },
    failed: (throwable: Throwable) -> Unit = { },
    complete: () -> Unit = { },
) {
    when (this) {
        is AsyncState.Idle -> idle()
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

package net.samystudio.beaver.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.R

fun AppCompatActivity.showErrorDialog(
    @IdRes hostId: Int,
    throwable: Throwable? = null,
    title: String? = null,
    message: String? = null,
    positiveButtonRes: Int = R.string.ok,
    negativeButtonRes: Int = 0,
    cancelable: Boolean = true,
    requestCode: Int = 0
) {
    findNavController(hostId).navigate(
        getNavDirection(
            throwable,
            title,
            message,
            positiveButtonRes,
            negativeButtonRes,
            cancelable,
            requestCode
        )
    )
    throwable?.let { FirebaseCrashlytics.getInstance().recordException(it) }
}

fun Fragment.showErrorDialog(
    throwable: Throwable? = null,
    title: String? = null,
    message: String? = null,
    positiveButtonRes: Int = R.string.ok,
    negativeButtonRes: Int = 0,
    cancelable: Boolean = true,
    requestCode: Int = 0
) {
    findNavController().navigate(
        getNavDirection(
            throwable,
            title,
            message,
            positiveButtonRes,
            negativeButtonRes,
            cancelable,
            requestCode
        )
    )
    throwable?.let { FirebaseCrashlytics.getInstance().recordException(it) }
}

private fun getNavDirection(
    throwable: Throwable? = null,
    title: String? = null,
    message: String? = null,
    positiveButtonRes: Int = R.string.ok,
    negativeButtonRes: Int = 0,
    cancelable: Boolean = true,
    requestCode: Int = 0
) = NavigationMainDirections.actionGlobalErrorDialog(
    throwable as? Exception,
    title,
    message ?: throwable?.message?.takeIf { throwable !is Exception },
    positiveButtonRes,
    negativeButtonRes,
    cancelable,
    requestCode,
)

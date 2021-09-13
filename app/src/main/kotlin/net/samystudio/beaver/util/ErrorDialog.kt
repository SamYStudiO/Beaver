package net.samystudio.beaver.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.samystudio.beaver.NavigationMainDirections
import net.samystudio.beaver.data.remote.retrofit.RetrofitException
import net.samystudio.beaver.ui.common.dialog.ErrorSource

/**
 *
 */
fun Fragment.showErrorDialog(
    errorSource: ErrorSource? = null,
    throwable: Throwable? = null,
    appCode: Int? = null,
    message: String? = null
) {
    findNavController().navigate(
        NavigationMainDirections.actionGlobalErrorDialog(
            when {
                errorSource != null -> errorSource
                (throwable as? RetrofitException)?.kind == RetrofitException.Kind.NETWORK -> ErrorSource.NETWORK
                throwable is RetrofitException -> ErrorSource.SERVER
                else -> ErrorSource.APP
            },
            appCode?.toString(),
            (throwable as? RetrofitException)?.code?.toString(),
            (throwable as? RetrofitException)?.errorBody?.code?.toString(),
            message ?: (throwable as? RetrofitException)?.errorBody?.message
                ?: throwable?.message
        )
    )

    throwable?.let {
        FirebaseCrashlytics.getInstance().recordException(it)
    }
}

package net.samystudio.beaver.ui.common.dialog

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import javax.inject.Inject

/**
 * An Alert dialog to show error consistently across app.
 * At least an [ErrorSource] must be set.
 * Optionally an http or app code may be pass, with http code an extra server code may be passed as
 * well.
 * And of course a message may be passed.
 */
@AndroidEntryPoint
class ErrorDialog : AlertDialog() {
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    override fun onPrepareDialogBuilder(builder: MaterialAlertDialogBuilder) {
        super.onPrepareDialogBuilder(builder)
        val source = arguments?.get(KEY_ERROR_SOURCE) as? ErrorSource ?: ErrorSource.SERVER
        val httpOrAppCode = arguments?.getString(KEY_ERROR_HTTP_OR_APP_CODE)
        val serverCode = arguments?.getString(KEY_ERROR_SERVER_CODE)
            ?.let { if (it == httpOrAppCode) null else it }
        val message = arguments?.getString(KEY_ERROR_MESSAGE)

        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage(
            when (source) {
                ErrorSource.NETWORK -> getString(R.string.error_connexion_required)
                ErrorSource.SERVER, ErrorSource.APP, ErrorSource.SYSTEM -> {
                    "%s%s".format(
                        getString(R.string.error_title).replace(
                            "{code}",
                            "%s%s%s".format(
                                "${source.code}",
                                if (httpOrAppCode != null) "_$httpOrAppCode" else "",
                                if (serverCode != null) "_$serverCode" else ""
                            )
                        ),
                        if (message.isNullOrBlank()) "" else "\n\n\"$message\""
                    )
                }
            }
        )
    }

    companion object {
        const val KEY_ERROR_SOURCE = "source"
        const val KEY_ERROR_HTTP_OR_APP_CODE = "httpOrAppCode"
        const val KEY_ERROR_SERVER_CODE = "serverCode"
        const val KEY_ERROR_MESSAGE = "message"
    }
}

enum class ErrorSource(val code: Int) {
    // IO error
    NETWORK(0),
    // HTTP error 3xx, 4xx, 5xx
    SERVER(1),
    // Internal app error
    APP(2),
    // System error
    SYSTEM(3)
}

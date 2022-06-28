package net.samystudio.beaver.ui.common.dialog

import android.graphics.drawable.Drawable
import androidx.core.text.HtmlCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class ErrorDialog : AlertDialog() {
    override fun onPrepareDialogBuilder(builder: MaterialAlertDialogBuilder) {
        super.onPrepareDialogBuilder(builder)
        val exception = arguments?.getSerializable(KEY_ERROR_EXCEPTION) as? Exception
        val isNetworkError = exception is IOException
        val title = arguments?.getString(KEY_ERROR_TITLE)
        val message = arguments?.getString(KEY_ERROR_MESSAGE)

        builder.setIcon(R.mipmap.ic_launcher)
        title?.let { builder.setTitle(HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)) }
            ?: builder.setTitle(R.string.error_title)

        builder.setMessage(
            HtmlCompat.fromHtml(
                "%s%s".format(
                    when {
                        isNetworkError -> getString(R.string.error_connexion_required)
                        else -> message?.takeIf { it.isNotBlank() }
                            ?: getString(R.string.error_message)
                    },
                    "<br/><br/>\"<small><i>%s</i></small>\"".format(
                        when (exception) {
                            is HttpException -> "${exception.code()} - ${exception.message ?: ""}"
                            else -> exception?.message ?: ""
                        }
                    )
                ),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        )
    }

    override fun handleIcon(drawable: Drawable?): Drawable? = drawable

    companion object {
        const val KEY_ERROR_EXCEPTION = "exception"
        const val KEY_ERROR_TITLE = "title"
        const val KEY_ERROR_MESSAGE = "message"
    }
}

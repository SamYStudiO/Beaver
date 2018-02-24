@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.dialog

import android.os.Bundle
import android.support.annotation.IntDef

class DialogResult @JvmOverloads constructor(@ResultCode @get:ResultCode var code: Long,
                                             var bundle: Bundle? = null)
{
    val isOk: Boolean
        get() = code == RESULT_OK
    val isCancelled: Boolean
        get() = code == RESULT_CANCELLED
    val isError: Boolean
        get() = code == RESULT_ERROR

    fun hasBundle() = bundle != null

    fun hasKey(key: String) = hasBundle() && bundle!!.containsKey(key)

    fun hasNonNullKey(key: String) =
        hasBundle() && bundle!!.containsKey(key) && bundle!!.get(key) != null

    fun <T> getKey(key: String) = bundle?.get(key)

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(RESULT_OK, RESULT_CANCELLED, RESULT_ERROR)
    annotation class ResultCode

    companion object
    {
        const val RESULT_CANCELLED = 0L
        const val RESULT_OK = 1L
        const val RESULT_ERROR = 2L
    }
}

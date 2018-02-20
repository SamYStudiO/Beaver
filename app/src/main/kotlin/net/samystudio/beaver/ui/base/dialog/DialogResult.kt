@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.dialog

import android.os.Bundle
import android.support.annotation.IntDef

class DialogResult
{
    @ResultCode
    @get:ResultCode
    var code: Long = 0
    var bundle: Bundle? = null
    var error: Throwable? = null
    val isOk: Boolean
        get() = code == RESULT_OK
    val isCancelled: Boolean
        get() = code == RESULT_CANCELLED
    val isError: Boolean
        get() = code == RESULT_ERROR

    @JvmOverloads
    constructor(@ResultCode code: Long, bundle: Bundle? = null)
    {
        this.code = code
        this.bundle = bundle
    }

    constructor(error: Throwable?)
    {
        this.code = RESULT_ERROR
        this.error = error
    }

    fun hasBundle() = bundle != null

    fun hasKey(key: String) = hasBundle() && bundle!!.containsKey(key)

    fun hasNonNullKey(key: String) = hasBundle() && bundle!!.containsKey(key) && bundle!!.get(key) != null

    fun <T> getKey(key: String) = bundle?.get(key)

    fun hasError() = error != null

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

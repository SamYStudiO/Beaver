@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.dialog

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.DialogInterface
import android.os.Bundle
import net.samystudio.beaver.ui.base.fragment.BaseFragment

abstract class BaseResultDialog : BaseDialog()
{
    protected var resultCode: Long = 0
    protected var resultError: Throwable? = null
    protected var resultData: Bundle? = null

    @JvmOverloads
    fun setResultAsOk(resultData: Bundle? = null, dismiss: Boolean = false)
    {
        setResultAs(DialogResult.RESULT_OK, resultData, dismiss)
    }

    @JvmOverloads
    fun setResultAsCancelled(dismiss: Boolean = false)
    {
        setResultAs(DialogResult.RESULT_CANCELLED, null as Bundle?, dismiss)
    }

    @JvmOverloads
    fun setResultAsError(error: Throwable? = null, dismiss: Boolean = false)
    {
        setResultAs(DialogResult.RESULT_ERROR, error, dismiss)
    }

    @JvmOverloads
    fun setResultAs(@DialogResult.ResultCode resultCode: Long,
                    resultData: Bundle? = null,
                    dismiss: Boolean = false)
    {
        this.resultCode = resultCode
        this.resultData = resultData

        if (dismiss)
            dismiss()
    }

    @JvmOverloads
    fun setResultAs(@DialogResult.ResultCode resultCode: Long,
                    error: Throwable?,
                    dismiss: Boolean = false)
    {
        this.resultCode = resultCode
        this.resultError = error

        if (dismiss)
            dismiss()
    }

    override fun onCancel(dialog: DialogInterface)
    {
        setResultAsCancelled()

        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface)
    {
        super.onDismiss(dialog)

        if (targetFragment != null && targetFragment is BaseFragment)
        {
            if (ProcessLifecycleOwner
                            .get()
                            .lifecycle
                            .currentState
                            .isAtLeast(getLifecycleStateFromOrder(
                                    if (arguments != null)
                                        arguments!!.getInt(KEY_LIFECYCLE_STATE, 0)
                                    else 0)))
            {
                val baseFragment = targetFragment as BaseFragment

                if (resultCode == DialogResult.RESULT_ERROR)
                    baseFragment.onDialogResult(targetRequestCode,
                                                DialogResult(resultError))
                else
                    baseFragment.onDialogResult(targetRequestCode,
                                                DialogResult(resultCode, resultData))
            }
        }
    }

    companion object
    {
        private const val KEY_LIFECYCLE_STATE = "lifecycleState"

        fun getLifecycleStateBundle(state: Lifecycle.State): Bundle
        {
            val bundle = Bundle()
            bundle.putInt(KEY_LIFECYCLE_STATE, Lifecycle.State.DESTROYED.compareTo(state))
            return bundle
        }

        private fun getLifecycleStateFromOrder(order: Int): Lifecycle.State
        {
            return Lifecycle.State.values().firstOrNull { Lifecycle.State.DESTROYED.compareTo(it) == order }
                   ?: Lifecycle.State.DESTROYED
        }
    }
}

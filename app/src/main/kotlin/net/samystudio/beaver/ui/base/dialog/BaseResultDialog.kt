@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.dialog

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.DialogInterface
import android.os.Bundle
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

abstract class BaseResultDialog : BaseDialog()
{
    @DialogResult.ResultCode
    protected var resultCode: Long = DialogResult.RESULT_OK
    protected var resultData: Bundle? = null

    @JvmOverloads
    fun dismissWithResult(@DialogResult.ResultCode resultCode: Long, resultData: Bundle? = null,
                          @FragmentNavigationManager.StateLossPolicy stateLossPolicy: Long?)
    {
        this.resultCode = resultCode
        this.resultData = resultData

        dismiss(stateLossPolicy)
    }

    override fun onCancel(dialog: DialogInterface)
    {
        super.onCancel(dialog)

        this.resultCode = DialogResult.RESULT_CANCELLED
        this.resultData = null
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

                baseFragment.onDialogResult(targetRequestCode, DialogResult(resultCode, resultData))

                resultCode = DialogResult.RESULT_OK
                resultData = null
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

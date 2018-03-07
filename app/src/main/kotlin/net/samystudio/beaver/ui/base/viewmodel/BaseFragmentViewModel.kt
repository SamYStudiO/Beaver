package net.samystudio.beaver.ui.base.viewmodel

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper

abstract class BaseFragmentViewModel
constructor(protected val activityViewModel: BaseActivityViewModel) : BaseViewControllerViewModel()
{
    protected abstract val title: String?

    override fun handleCreate()
    {
        super.handleCreate()

        if (title != null)
            activityViewModel.title = title
    }

    /**
     * Convenient method to handle all [android.support.v4.app.Fragment] parameters at once.
     * This is called from view onResume, so may be called several time during view lifecycle. You
     * should make sure you've not already consume these parameters otherwise in some circumstance
     * it could lead to unexpected behaviours (for example a [android.widget.Toast] popping though
     * it already has been consumed).
     *
     * @param argument [Bundle] same as [android.support.v4.app.Fragment.getArguments].
     * @param savedInstanceState [Bundle] same as [android.support.v4.app.Fragment.onCreate].
     * @param requestCode same as [android.support.v4.app.Fragment.onActivityResult].
     * @param resultCode same as [android.support.v4.app.Fragment.onActivityResult].
     * @param data same as [android.support.v4.app.Fragment.onActivityResult].
     */
    @CallSuper
    open fun handleState(argument: Bundle?,
                         savedInstanceState: Bundle?,
                         requestCode: Int?,
                         resultCode: Int?,
                         data: Intent?)
    {
    }
}
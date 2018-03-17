package net.samystudio.beaver.ui.base.viewmodel

import android.os.Bundle

abstract class BaseFragmentViewModel
constructor(protected val activityViewModel: BaseActivityViewModel) : BaseViewControllerViewModel()
{
    protected abstract val title: String?

    override fun handleCreate(savedInstanceState: Bundle?)
    {
        super.handleCreate(savedInstanceState)

        if (title != null)
            activityViewModel.title = title
    }

    open fun handleArguments(argument: Bundle)
    {
    }
}
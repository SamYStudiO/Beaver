package net.samystudio.beaver.ui.base.viewmodel

import android.os.Bundle

abstract class BaseControllerViewModel
    : BaseViewControllerViewModel()
{
    protected abstract val title: String?

    open fun handleArguments(argument: Bundle)
    {
    }
}
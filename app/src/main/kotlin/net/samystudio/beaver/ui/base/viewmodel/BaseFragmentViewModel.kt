package net.samystudio.beaver.ui.base.viewmodel

import android.os.Bundle

abstract class BaseFragmentViewModel : BaseViewControllerViewModel() {
    open fun handleArguments(argument: Bundle) {}
}
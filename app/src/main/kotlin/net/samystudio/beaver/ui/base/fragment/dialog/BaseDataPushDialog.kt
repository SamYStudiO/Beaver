@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment.dialog

import net.samystudio.beaver.ui.base.viewmodel.BaseViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel

abstract class BaseDataPushDialog<D, VM> : BaseDialog<VM>()
        where VM : BaseViewModel, VM : DataPushViewModel
{
    abstract fun onPushData()
}
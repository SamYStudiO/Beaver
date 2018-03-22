package net.samystudio.beaver.ui.base.viewmodel.factory

import android.arch.lifecycle.ViewModel
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@ControllerScope
class ControllerViewModelFactory
@Inject
constructor(creators: Map<Class<out BaseControllerViewModel>,
        @JvmSuppressWildcards Provider<BaseControllerViewModel>>) :
    BaseViewModelFactory(creators as Map<Class<out ViewModel>, Provider<ViewModel>>)
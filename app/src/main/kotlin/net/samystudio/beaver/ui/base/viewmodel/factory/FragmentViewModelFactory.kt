package net.samystudio.beaver.ui.base.viewmodel.factory

import android.arch.lifecycle.ViewModel
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@FragmentScope
class FragmentViewModelFactory
@Inject
constructor(creators: Map<Class<out BaseFragmentViewModel>,
        @JvmSuppressWildcards Provider<BaseFragmentViewModel>>) :
    BaseViewModelFactory(creators as Map<Class<out ViewModel>, Provider<ViewModel>>)
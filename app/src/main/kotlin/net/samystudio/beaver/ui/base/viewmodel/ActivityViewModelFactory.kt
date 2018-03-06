package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.ViewModel
import net.samystudio.beaver.di.scope.ActivityScope
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
@ActivityScope
class ActivityViewModelFactory
@Inject
constructor(creators: Map<Class<out BaseActivityViewModel>,
        @JvmSuppressWildcards Provider<BaseActivityViewModel>>) :
    BaseViewModelFactory(creators as Map<Class<out ViewModel>, Provider<ViewModel>>)
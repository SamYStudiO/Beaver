@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.viewmodel.factory.FragmentViewModelFactory

@Module
abstract class BaseViewModelFragmentModule {
    @Binds
    @FragmentScope
    @FragmentContext
    abstract fun bindViewModelFactory(viewModelFactory: FragmentViewModelFactory): ViewModelProvider.Factory
}

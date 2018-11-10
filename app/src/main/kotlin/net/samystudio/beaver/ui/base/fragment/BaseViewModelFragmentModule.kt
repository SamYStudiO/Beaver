@file:Suppress("unused")

package net.samystudio.beaver.ui.base.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.viewmodel.factory.FragmentViewModelFactory

@Module
abstract class BaseViewModelFragmentModule {
    @Binds
    @FragmentScope
    @FragmentContext
    abstract fun bindViewModelFactory(viewModelFactory: FragmentViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object {
        @Provides
        @FragmentScope
        @FragmentContext
        @JvmStatic
        fun provideViewModelProvider(
            fragment: Fragment,
            @FragmentContext viewModelFactory: ViewModelProvider.Factory
        ): ViewModelProvider =
            ViewModelProviders.of(fragment, viewModelFactory)
    }
}

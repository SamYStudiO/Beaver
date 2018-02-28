package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ext.GlideApp
import net.samystudio.beaver.ext.GlideRequests
import net.samystudio.beaver.ui.common.viewmodel.FragmentViewModelFactory

@Module
abstract class BaseFragmentModule
{
    @Binds
    @FragmentScope
    @FragmentLevel
    abstract fun bindViewModelFactory(viewModelFactory: FragmentViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object
    {
        @Provides
        @FragmentScope
        @FragmentLevel
        @JvmStatic
        fun provideViewModelProvider(fragment: Fragment,
                                     @FragmentLevel
                                     viewModelFactory: ViewModelProvider.Factory): ViewModelProvider =
            ViewModelProviders.of(fragment, viewModelFactory)

        @Provides
        @FragmentScope
        @FragmentLevel
        @JvmStatic
        fun provideGlideRequests(fragment: Fragment): GlideRequests =
            GlideApp.with(fragment)
    }
}

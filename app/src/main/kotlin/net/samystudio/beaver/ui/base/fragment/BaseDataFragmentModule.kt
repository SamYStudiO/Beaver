package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.common.model.ViewModelFactory

@Module(includes = [BaseFragmentModule::class])
abstract class BaseDataFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object
    {
        @Provides
        @FragmentScope
        @JvmStatic
        fun provideViewModelProviders(fragment: Fragment,
                                      viewModelFactory: ViewModelFactory): ViewModelProvider =
            ViewModelProviders.of(fragment, viewModelFactory)
    }
}
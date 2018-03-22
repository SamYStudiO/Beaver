@file:Suppress("unused")

package net.samystudio.beaver.ui.base.controller

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.base.viewmodel.factory.ControllerViewModelFactory

@Module
abstract class BaseControllerModule
{
    @Binds
    @ControllerScope
    abstract fun bindViewModelFactory(
        viewModelFactory: ControllerViewModelFactory): ViewModelProvider.Factory

    @Module
    companion object
    {
        @Provides
        @ControllerScope
        @JvmStatic
        fun provideViewModelStore(): ViewModelStore = ViewModelStore()

        @Provides
        @ControllerScope
        @JvmStatic
        fun provideViewModelProvider(viewModelStore: ViewModelStore,
                                     viewModelFactory: ViewModelProvider.Factory): ViewModelProvider =
            ViewModelProvider(viewModelStore, viewModelFactory)
    }
}
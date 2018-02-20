package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.scope.PerFragment
import net.samystudio.beaver.utils.ViewModelFactory

@Module(includes = [BaseFragmentModule::class])
abstract class BaseDataFragmentModule
{
    @Binds
    @PerFragment
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
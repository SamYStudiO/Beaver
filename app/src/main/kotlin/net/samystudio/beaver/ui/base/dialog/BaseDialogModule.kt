package net.samystudio.beaver.ui.base.dialog

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.utils.ViewModelFactory

@Module
abstract class BaseDialogModule
{
    @Binds
    @FragmentScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
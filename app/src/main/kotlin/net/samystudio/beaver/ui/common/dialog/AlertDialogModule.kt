package net.samystudio.beaver.ui.common.dialog

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.FragmentViewModelKey
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

@Module(includes = [BaseFragmentModule::class])
abstract class AlertDialogModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: AlertDialog): Fragment

    @Binds
    @FragmentScope
    @FragmentLevel
    abstract fun bindLifecycleOwner(fragment: AlertDialog): LifecycleOwner

    @Binds
    @IntoMap
    @FragmentViewModelKey(AlertDialogViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: AlertDialogViewModel): BaseFragmentViewModel
}

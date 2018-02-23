package net.samystudio.beaver.ui.launch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ViewModelKey
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseDataFragmentModule

@Module(includes = [BaseDataFragmentModule::class])
abstract class LaunchFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: LaunchFragment): Fragment

    @Binds
    @FragmentScope
    abstract fun bindLifeCycleOwner(fragment: LaunchFragment): LifecycleOwner

    @Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    @FragmentScope
    abstract fun bindLaunchViewModel(model: LaunchViewModel): ViewModel
}

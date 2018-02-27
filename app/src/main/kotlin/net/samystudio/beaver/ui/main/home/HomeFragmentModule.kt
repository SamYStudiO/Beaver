package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ViewModelKey
import net.samystudio.beaver.di.qualifier.FragmentLifecycle
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule

@Module(includes = [BaseFragmentModule::class])
abstract class HomeFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: HomeFragment): Fragment

    @Binds
    @FragmentScope
    @FragmentLifecycle
    abstract fun bindLifeCycleOwner(fragment: HomeFragment): LifecycleOwner

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    @FragmentScope
    abstract fun bindMainViewModel(model: HomeFragmentViewModel): ViewModel
}

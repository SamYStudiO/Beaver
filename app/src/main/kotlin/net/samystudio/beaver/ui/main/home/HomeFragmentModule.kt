package net.samystudio.beaver.ui.main.home

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
abstract class HomeFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: HomeFragment): Fragment

    @Binds
    @FragmentScope
    @FragmentLevel
    abstract fun bindLifecycleOwner(fragment: HomeFragment): LifecycleOwner

    @Binds
    @IntoMap
    @FragmentViewModelKey(HomeFragmentViewModel::class)
    @FragmentScope
    abstract fun bindViewModel(viewModel: HomeFragmentViewModel): BaseFragmentViewModel
}

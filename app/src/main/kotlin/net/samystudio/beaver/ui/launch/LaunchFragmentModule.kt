package net.samystudio.beaver.ui.launch

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule

@Module(includes = [BaseFragmentModule::class])
abstract class LaunchFragmentModule
{
    @Binds
    @FragmentScope
    abstract fun bindFragment(fragment: LaunchFragment): Fragment

    @Binds
    @FragmentScope
    abstract fun bindLifeCycleOwner(fragment: LaunchFragment): LifecycleOwner

    /*@Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    @PerFragment
    abstract fun bindLaunchViewModel(model: LaunchViewModel): ViewModel*/
}

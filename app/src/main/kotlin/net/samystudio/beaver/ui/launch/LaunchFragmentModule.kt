package net.samystudio.beaver.ui.launch

import android.arch.lifecycle.LifecycleOwner
import dagger.Binds
import dagger.Module
import net.samystudio.beaver.di.scope.PerFragment
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.fragment.BaseFragmentModule

@Module(includes = [BaseFragmentModule::class])
abstract class LaunchFragmentModule
{
    @Binds
    @PerFragment
    abstract fun bindFragment(fragment: LaunchFragment): BaseFragment

    @Binds
    @PerFragment
    abstract fun bindLifeCycleOwner(fragment: LaunchFragment): LifecycleOwner

    /*@Binds
    @IntoMap
    @ViewModelKey(LaunchViewModel::class)
    @PerFragment
    abstract fun bindLaunchViewModel(model: LaunchViewModel): ViewModel*/
}

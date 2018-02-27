package net.samystudio.beaver.ui.launch

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import net.samystudio.beaver.di.key.ViewModelKey
import net.samystudio.beaver.di.qualifier.ActivityLifecycle
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.activity.BaseActivityModule

@Module(includes = [BaseActivityModule::class])
abstract class LaunchActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: LaunchActivity): AppCompatActivity

    @Binds
    @ActivityScope
    @ActivityLifecycle
    abstract fun bindLifeCycleOwner(fragment: LaunchActivity): LifecycleOwner

    @Binds
    @IntoMap
    @ViewModelKey(LaunchActivityViewModel::class)
    @ActivityScope
    abstract fun bindLaunchActivityViewModel(fragment: LaunchActivityViewModel): ViewModel

    @ContributesAndroidInjector(modules = [LaunchFragmentModule::class])
    @FragmentScope
    abstract fun contributeLaunchFragment(): LaunchFragment
}

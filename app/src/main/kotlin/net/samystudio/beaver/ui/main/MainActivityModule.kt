package net.samystudio.beaver.ui.main

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
import net.samystudio.beaver.ui.main.home.HomeFragment
import net.samystudio.beaver.ui.main.home.HomeFragmentModule

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: MainActivity): AppCompatActivity

    @Binds
    @ActivityScope
    @ActivityLifecycle
    abstract fun bindLifeCycleOwner(fragment: MainActivity): LifecycleOwner

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @ActivityScope
    abstract fun bindMainActivityViewModel(fragment: MainActivityViewModel): ViewModel

    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    @FragmentScope
    abstract fun contributeMainFragment(): HomeFragment
}

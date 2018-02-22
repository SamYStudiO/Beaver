package net.samystudio.beaver.ui.launch

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.activity.BaseActivityModule

@Module(includes = [BaseActivityModule::class])
abstract class LaunchActivityModule
{
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: LaunchActivity): AppCompatActivity

    @ContributesAndroidInjector(modules = [LaunchFragmentModule::class])
    @FragmentScope
    abstract fun contributeLaunchFragment(): LaunchFragment
}

package net.samystudio.beaver.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.launch.LaunchActivity
import net.samystudio.beaver.ui.launch.LaunchActivityModule
import net.samystudio.beaver.ui.main.MainActivity
import net.samystudio.beaver.ui.main.MainActivityModule

@Module
abstract class ActivityBuildersModule
{
    @ContributesAndroidInjector(modules = [LaunchActivityModule::class])
    @ActivityScope
    abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @ActivityScope
    abstract fun contributeMainActivity(): MainActivity
}

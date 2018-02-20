package net.samystudio.beaver.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.PerActivity
import net.samystudio.beaver.ui.launch.LaunchActivity
import net.samystudio.beaver.ui.launch.LaunchActivityModule
import net.samystudio.beaver.ui.main.MainActivity
import net.samystudio.beaver.ui.main.MainActivityModule

@Module
abstract class ActivityBuildersModule
{
    @ContributesAndroidInjector(modules = [LaunchActivityModule::class])
    @PerActivity
    abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    @PerActivity
    abstract fun contributeMainActivity(): MainActivity
}

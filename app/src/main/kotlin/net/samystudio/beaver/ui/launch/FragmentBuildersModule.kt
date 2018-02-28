package net.samystudio.beaver.ui.launch

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.FragmentScope

@Module
abstract class FragmentBuildersModule
{
    @ContributesAndroidInjector(modules = [LaunchFragmentModule::class])
    @FragmentScope
    abstract fun contributeLaunchFragment(): LaunchFragment
}

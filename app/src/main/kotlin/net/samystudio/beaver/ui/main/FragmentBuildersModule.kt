package net.samystudio.beaver.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.main.home.HomeFragment
import net.samystudio.beaver.ui.main.home.HomeFragmentModule

@Module
abstract class FragmentBuildersModule
{
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    @FragmentScope
    abstract fun contributeHomeFragment(): HomeFragment
}

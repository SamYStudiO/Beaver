@file:Suppress("unused")

package net.samystudio.beaver.ui.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorFragment
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorFragmentModule
import net.samystudio.beaver.ui.main.home.HomeFragment
import net.samystudio.beaver.ui.main.home.HomeFragmentModule

@Module
abstract class MainActivityFragmentBuilderModule
{
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    @FragmentScope
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [AuthenticatorFragmentModule::class])
    @FragmentScope
    abstract fun contributeAuthenticatorFragment(): AuthenticatorFragment
}

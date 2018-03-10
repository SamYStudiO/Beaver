@file:Suppress("unused")

package net.samystudio.beaver.ui.authenticator

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.FragmentScope

@Module
abstract class AuthenticatorActivityFragmentBuilderModule
{
    @ContributesAndroidInjector(modules = [AuthenticatorFragmentModule::class])
    @FragmentScope
    abstract fun contributeAuthenticatorFragment(): AuthenticatorFragment
}

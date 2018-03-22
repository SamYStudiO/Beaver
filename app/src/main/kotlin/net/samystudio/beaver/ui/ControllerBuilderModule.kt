@file:Suppress("unused")

package net.samystudio.beaver.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.samystudio.beaver.di.scope.ControllerScope
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorController
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorControllerModule
import net.samystudio.beaver.ui.main.home.HomeController
import net.samystudio.beaver.ui.main.home.HomeControllerModule

@Module
abstract class ControllerBuilderModule
{
    @ContributesAndroidInjector(modules = [HomeControllerModule::class])
    @ControllerScope
    abstract fun contributeHomeController(): HomeController

    @ContributesAndroidInjector(modules = [AuthenticatorControllerModule::class])
    @ControllerScope
    abstract fun contributeAuthenticatorController(): AuthenticatorController
}
@file:Suppress("unused")

package net.samystudio.beaver.data.service

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Dagger injection for all services. If a service need injection it must be referenced here.
 */
@Module
abstract class ServiceBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributeAuthenticatorService(): AuthenticatorService
}

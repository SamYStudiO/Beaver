package net.samystudio.beaver.data.service

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributeAuthenticatorService(): AuthenticatorService
}

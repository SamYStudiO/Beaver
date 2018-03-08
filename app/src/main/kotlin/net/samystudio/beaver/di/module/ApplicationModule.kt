package net.samystudio.beaver.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.data.service.ServiceBuilderModule
import net.samystudio.beaver.di.component.GlideComponent
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.ActivityBuilderModule
import javax.inject.Singleton

@Module(subcomponents = [GlideComponent::class],
        includes = [CrashlyticsModule::class, TimberModule::class,
            AndroidSupportInjectionModule::class, ActivityBuilderModule::class,
            ServiceBuilderModule::class, SystemServiceModule::class, NetworkModule::class,
            FirebaseModule::class])
abstract class ApplicationModule
{
    @Binds
    @Singleton
    abstract fun bindApplication(application: BeaverApplication): Application

    @Module
    companion object
    {
        @Provides
        @Singleton
        @ApplicationContext
        @JvmStatic
        fun provideApplicationContext(application: BeaverApplication): Context = application
    }
}

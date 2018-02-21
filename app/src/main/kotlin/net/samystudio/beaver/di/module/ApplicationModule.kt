package net.samystudio.beaver.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.ActivityBuildersModule
import javax.inject.Singleton

@Module(includes = [AndroidSupportInjectionModule::class, ActivityBuildersModule::class, DataModule::class, NetModule::class])
abstract class ApplicationModule
{
    @Binds
    @Singleton
    abstract fun bindApplication(application: BeaverApplication): Application

    @Module
    companion object
    {
        @Provides
        @ApplicationContext
        @Singleton
        @JvmStatic
        fun provideApplicationContext(application: BeaverApplication): Context = application
    }
}

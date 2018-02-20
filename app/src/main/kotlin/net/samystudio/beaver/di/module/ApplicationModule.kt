package net.samystudio.beaver.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.HighwayApplication
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.ActivityBuildersModule
import javax.inject.Singleton

@Module(includes = [AndroidSupportInjectionModule::class, ActivityBuildersModule::class, DataModule::class, NetModule::class])
abstract class ApplicationModule
{
    @Module
    companion object
    {
        @Provides
        @ApplicationContext
        @Singleton
        @JvmStatic
        fun provideApplicationContext(application: HighwayApplication): Context = application
    }
}

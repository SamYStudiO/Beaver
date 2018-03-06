package net.samystudio.beaver.di.module

import android.app.Application
import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.di.component.GlideComponent
import net.samystudio.beaver.di.qualifier.ApplicationContext
import timber.log.Timber
import javax.inject.Singleton

@Module(subcomponents = [GlideComponent::class])
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

        @Provides
        @Singleton
        @JvmStatic
        fun provideCrashlyticsCore(): CrashlyticsCore =
            CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideCrashlytics(crashlyticsCore: CrashlyticsCore): Crashlytics =
            Crashlytics.Builder().core(crashlyticsCore).build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideFabric(@ApplicationContext
                          context: Context, crashlytics: Crashlytics): Fabric =
            Fabric.with(context, crashlytics)

        @Provides
        @Singleton
        @JvmStatic
        fun provideTimberTree(): Timber.Tree =
            object : Timber.DebugTree()
            {
                override fun isLoggable(tag: String?, priority: Int) =
                    BuildConfig.DEBUG || priority >= Log.INFO
            }
    }
}

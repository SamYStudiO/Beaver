@file:Suppress("unused")

package net.samystudio.beaver.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import net.samystudio.beaver.BeaverApplication
import net.samystudio.beaver.data.TrimMemory
import net.samystudio.beaver.data.service.ServiceBuilderModule
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.ActivityBuilderModule
import javax.inject.Singleton

@Module(
    includes = [CrashlyticsModule::class, TimberModule::class, AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class, ServiceBuilderModule::class, SystemServiceModule::class,
        NetworkModule::class, FirebaseModule::class, DataModule::class, PicassoModule::class]
)
abstract class ApplicationModule {
    @Binds
    @Singleton
    abstract fun bindApplication(application: BeaverApplication): Application

    @Binds
    @Singleton
    @ApplicationContext
    abstract fun bindApplicationContext(application: Application): Context

    companion object {
        /**
         * Add to this list any application level data implementing [TrimMemory] that should clear
         * some cache when app is running low on memory.
         */
        @Provides
        @Singleton
        fun provideTrimMemoryList(picassoTrimMemory: PicassoModule.PicassoTrimMemory): ArrayList<TrimMemory> =
            mutableListOf<TrimMemory>(picassoTrimMemory) as ArrayList<TrimMemory>
    }
}
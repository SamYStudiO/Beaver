@file:Suppress("unused")

package net.samystudio.beaver.di.module

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.TrimMemory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideTimberTree(): Timber.Tree =
        object : Timber.DebugTree() {
            override fun isLoggable(tag: String?, priority: Int) =
                BuildConfig.DEBUG || priority >= Log.INFO
        }

    /**
     * Add to this list any application level data implementing [TrimMemory] that should clear
     * some cache when app is running low on memory.
     */
    @Provides
    @Singleton
    fun provideTrimMemoryList(picassoTrimMemory: PicassoModule.PicassoTrimMemory): ArrayList<TrimMemory> =
        mutableListOf<TrimMemory>(picassoTrimMemory) as ArrayList<TrimMemory>
}

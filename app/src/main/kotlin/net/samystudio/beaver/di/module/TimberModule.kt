package net.samystudio.beaver.di.module

import android.util.Log
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.BuildConfig
import timber.log.Timber
import javax.inject.Singleton

/**
 * https://github.com/JakeWharton/timber
 */
@Module
object TimberModule {
    @Provides
    @Singleton
    fun provideTimberTree(): Timber.Tree =
        object : Timber.DebugTree() {
            override fun isLoggable(tag: String?, priority: Int) =
                BuildConfig.DEBUG || priority >= Log.INFO
        }
}
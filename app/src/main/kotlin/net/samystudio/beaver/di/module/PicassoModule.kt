package net.samystudio.beaver.di.module

import android.content.ComponentCallbacks2
import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.TrimMemory
import net.samystudio.beaver.di.qualifier.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Module
object PicassoModule {
    @Provides
    @Singleton
    fun provideOkHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader =
        OkHttp3Downloader(okHttpClient)

    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso =
        Picasso.Builder(context)
            .downloader(okHttp3Downloader)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .loggingEnabled(BuildConfig.DEBUG)
            .build()

    @Singleton
    class PicassoTrimMemory @Inject constructor(private val picasso: Picasso) : TrimMemory {
        override fun onTrimMemory(level: Int) {
            when (level) {
                ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN,
                ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
                ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> picasso.evictAll()
                else -> {
                }
            }
        }
    }
}
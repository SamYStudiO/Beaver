package net.samystudio.beaver.di.module

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.di.qualifier.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
object PicassoModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader =
        OkHttp3Downloader(okHttpClient)

    @Provides
    @Singleton
    @JvmStatic
    fun providePicasso(@ApplicationContext context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso =
        Picasso.Builder(context)
            .downloader(okHttp3Downloader)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
}
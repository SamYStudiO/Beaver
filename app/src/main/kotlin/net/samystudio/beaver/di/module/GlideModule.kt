package net.samystudio.beaver.di.module

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
object GlideModule
{
    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor
    {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE

        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpUrlLoaderFactory(okHttpClient: OkHttpClient): OkHttpUrlLoader.Factory =
        OkHttpUrlLoader.Factory(okHttpClient)
}

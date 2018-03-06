package net.samystudio.beaver.di.module

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import dagger.Module
import dagger.Provides
import net.samystudio.beaver.di.qualifier.GlideContext
import net.samystudio.beaver.di.scope.GlideScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
object GlideModule
{
    @Provides
    @GlideScope
    @GlideContext
    @JvmStatic
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    @Provides
    @GlideScope
    @JvmStatic
    fun provideOkHttpUrlLoaderFactory(@GlideContext
                                      okHttpClient: OkHttpClient): OkHttpUrlLoader.Factory =
        OkHttpUrlLoader.Factory(okHttpClient)
}

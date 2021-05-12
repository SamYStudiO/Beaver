package net.samystudio.beaver.di

import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Provides
    @Singleton
    @OkHttpCoilQualifier
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .cache(CoilUtils.createDefaultCache(context))
            .build()

    @Provides
    @Singleton
    fun provideCoil(
        @ApplicationContext context: Context,
        @OkHttpCoilQualifier okHttpClient: OkHttpClient,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .okHttpClient { okHttpClient }
            .crossfade(true)
            .build()
}

package net.samystudio.beaver.di

import android.content.ComponentCallbacks2
import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.data.TrimMemory
import okhttp3.OkHttpClient
import javax.inject.Inject
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

    @Singleton
    class CoilTrimMemory @Inject constructor(private val imageLoader: ImageLoader) : TrimMemory {
        override fun onTrimMemory(level: Int) {
            when (level) {
                ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN,
                ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
                ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
                    imageLoader.bitmapPool.clear()
                    imageLoader.memoryCache.clear()
                }
                else -> Unit
            }
        }
    }
}

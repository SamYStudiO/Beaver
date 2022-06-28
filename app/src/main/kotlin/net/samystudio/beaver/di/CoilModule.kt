package net.samystudio.beaver.di

import android.content.ComponentCallbacks2
import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.samystudio.beaver.data.TrimMemory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {
    @Provides
    @Singleton
    @OkHttpCoilQualifier
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideCoilDiskCache(@ApplicationContext context: Context): DiskCache =
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("image_cache"))
            .build()

    @Provides
    @Singleton
    fun provideCoil(
        @ApplicationContext context: Context,
        @OkHttpCoilQualifier okHttpClient: OkHttpClient,
        diskCache: DiskCache,
    ): ImageLoader =
        ImageLoader.Builder(context)
            .okHttpClient { okHttpClient }
            .diskCache { diskCache }
            .crossfade(true)
            .build()
}

@Singleton
class CoilTrimMemory @Inject constructor(private val imageLoader: ImageLoader) : TrimMemory {
    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN,
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL ->
                imageLoader.memoryCache?.clear()
            else ->
                Unit
        }
    }
}

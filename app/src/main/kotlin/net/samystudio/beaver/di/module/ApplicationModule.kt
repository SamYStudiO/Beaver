@file:Suppress("unused")

package net.samystudio.beaver.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import net.samystudio.beaver.data.TrimMemory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    /**
     * Add to this list any application level data implementing [TrimMemory] that should clear
     * some cache when app is running low on memory.
     */
    @Provides
    @Singleton
    fun provideTrimMemoryList(picassoTrimMemory: PicassoModule.PicassoTrimMemory): ArrayList<TrimMemory> =
        mutableListOf<TrimMemory>(picassoTrimMemory) as ArrayList<TrimMemory>
}
package net.samystudio.beaver.data

/**
 * @see net.samystudio.beaver.di.module.ApplicationModule.provideTrimMemoryList
 * @see android.content.ComponentCallbacks2.onTrimMemory
 */
interface TrimMemory {
    fun onTrimMemory(level: Int)
}
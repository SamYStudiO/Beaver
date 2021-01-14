package net.samystudio.beaver.data

import android.content.ComponentCallbacks2
import net.samystudio.beaver.di.ApplicationModule

/**
 * @see ApplicationModule.provideTrimMemoryList
 * @see ComponentCallbacks2.onTrimMemory
 */
interface TrimMemory {
    fun onTrimMemory(level: Int)
}

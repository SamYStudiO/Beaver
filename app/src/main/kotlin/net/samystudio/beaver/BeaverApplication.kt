package net.samystudio.beaver

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import net.samystudio.beaver.data.TrimMemory
import javax.inject.Inject

@HiltAndroidApp
class BeaverApplication : Application(), Configuration.Provider, ImageLoaderFactory {
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var trimMemoryList: Array<TrimMemory>

    @Inject
    lateinit var workConfiguration: Configuration

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        trimMemoryList.forEach { it.onTrimMemory(level) }
    }

    override fun getWorkManagerConfiguration() = workConfiguration

    override fun newImageLoader(): ImageLoader = imageLoader
}

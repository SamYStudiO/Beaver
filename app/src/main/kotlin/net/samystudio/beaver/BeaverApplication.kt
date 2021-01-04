package net.samystudio.beaver

import android.app.Application
import androidx.work.Configuration
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import net.samystudio.beaver.data.TrimMemory
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BeaverApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var timberTree: Timber.Tree

    @Inject
    lateinit var trimMemoryList: ArrayList<TrimMemory>

    @Inject
    lateinit var workConfiguration: Configuration

    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Timber.plant(timberTree)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        trimMemoryList.forEach { it.onTrimMemory(level) }
    }

    override fun getWorkManagerConfiguration() = workConfiguration
}

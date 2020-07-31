package net.samystudio.beaver

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import net.samystudio.beaver.data.TrimMemory
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BeaverApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var timberTree: Timber.Tree

    @Inject
    lateinit var trimMemoryList: ArrayList<TrimMemory>

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Timber.plant(timberTree)

        // Launch screen timeout, this is not material guideline compliant but client is king and
        // most want it displayed longer, just remove if client is material compliant ^^.
        Thread.sleep(1000)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        trimMemoryList.forEach { it.onTrimMemory(level) }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
package net.samystudio.beaver

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.android.support.DaggerApplication
import net.samystudio.beaver.data.TrimMemory
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import net.samystudio.beaver.di.module.ApplicationModule
import net.samystudio.beaver.di.module.FirebaseModule
import net.samystudio.beaver.di.module.TimberModule
import timber.log.Timber
import javax.inject.Inject

class BeaverApplication : DaggerApplication() {
    private val applicationInjector = DaggerApplicationComponent.factory().create(this)

    /**
     * @see FirebaseModule.provideFirebaseCrashlytics
     */
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    /**
     * @see TimberModule.provideTimberTree
     */
    @Inject
    lateinit var timberTree: Timber.Tree

    /**
     * @see ApplicationModule.provideTrimMemoryList
     */
    @Inject
    lateinit var trimMemoryList: ArrayList<TrimMemory>

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

    override fun applicationInjector() = applicationInjector
}
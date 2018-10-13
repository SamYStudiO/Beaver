@file:Suppress("ProtectedInFinal", "unused")

package net.samystudio.beaver

import com.evernote.android.state.StateSaver
import com.squareup.leakcanary.LeakCanary
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class BeaverApplication : DaggerApplication() {
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    /**
     * @see [net.samystudio.beaver.di.module.CrashlyticsModule.provideFabric]
     */
    @Inject
    protected lateinit var fabric: Fabric
    /**
     * @see [net.samystudio.beaver.di.module.TimberModule.provideTimberTree]
     */
    @Inject
    protected lateinit var timberTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this))
            return

        LeakCanary.install(this)
        Timber.plant(timberTree)
        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true)

        // Launch screen timeout, this is not material guideline compliant but client is king and
        // most want it displayed longer, just remove if client is material compliant ^^.
        Thread.sleep(1000)
    }

    override fun applicationInjector() = applicationInjector
}
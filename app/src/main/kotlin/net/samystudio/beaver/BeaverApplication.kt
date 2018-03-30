@file:Suppress("ProtectedInFinal", "unused")

package net.samystudio.beaver

import com.bluelinelabs.conductor.Controller
import com.ivianuu.contributer.conductor.HasControllerInjector
import com.squareup.leakcanary.LeakCanary
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class BeaverApplication : DaggerApplication(), HasControllerInjector {
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    @Inject
    protected lateinit var controllerInjector: DispatchingAndroidInjector<Controller>
    @Inject
    protected lateinit var fabric: Fabric
    @Inject
    protected lateinit var timberTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this))
            return

        LeakCanary.install(this)

        Timber.plant(timberTree)

        // Launch screen timeout, this is not material guideline compliant but client is king and
        // most want it displayed longer, just remove if client is material compliant ^^.
        Thread.sleep(1000)
    }

    override fun applicationInjector() = applicationInjector
    override fun controllerInjector() = controllerInjector
}
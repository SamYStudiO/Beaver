@file:Suppress("ProtectedInFinal", "unused")

package net.samystudio.beaver

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import com.bluelinelabs.conductor.Controller
import com.ivianuu.contributer.conductor.HasControllerInjector
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class BeaverApplication : DaggerApplication(), HasControllerInjector
{
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    @Inject
    protected lateinit var controllerInjector: DispatchingAndroidInjector<Controller>
    @Inject
    protected lateinit var fabric: Fabric
    @Inject
    protected lateinit var timberTree: Timber.Tree

    override fun onCreate()
    {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this))
            return

        LeakCanary.install(this)

        Timber.plant(timberTree)
        logKeyHash()

        // Launch screen timeout, this is not material guideline compliant but client is king and
        // most want it displayed longer, just remove if client is material compliant ^^.
        Thread.sleep(1000)
    }

    public override fun applicationInjector() = applicationInjector

    override fun controllerInjector(): AndroidInjector<Controller> = controllerInjector

    /**
     * App key hash, may be useful with Facebook for example.
     */
    @SuppressLint("PackageManagerGetSignatures")
    private fun logKeyHash()
    {
        if (!BuildConfig.DEBUG)
            return

        try
        {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)

            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Timber.d("logKeyHash: %s", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e: PackageManager.NameNotFoundException)
        {
            Timber.w(e)
        }
        catch (e: NoSuchAlgorithmException)
        {
            Timber.w(e)
        }
    }
}
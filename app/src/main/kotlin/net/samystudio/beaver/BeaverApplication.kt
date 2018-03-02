package net.samystudio.beaver

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class BeaverApplication : DaggerApplication()
{
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    override fun onCreate()
    {
        initCrashReport()

        super.onCreate()

        initLogging()
        logKeyHash()
    }

    public override fun applicationInjector() = applicationInjector

    private fun initCrashReport()
    {
        Fabric.with(this,
                    Crashlytics.Builder()
                        .core(CrashlyticsCore.Builder()
                                  .disabled(BuildConfig.DEBUG)
                                  .build())
                        .build())
    }

    private fun initLogging()
    {
        Timber.plant(object : Timber.DebugTree()
                     {
                         override fun isLoggable(tag: String?, priority: Int) =
                             BuildConfig.DEBUG || priority >= Log.INFO
                     })
    }

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
            val info = packageManager.getPackageInfo(packageName,
                                                     PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Timber.d("logKeyHash: %s",
                         Base64.encodeToString(md.digest(), Base64.DEFAULT))
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

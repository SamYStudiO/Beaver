@file:Suppress("ProtectedInFinal")

package net.samystudio.beaver

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

class BeaverApplication : DaggerApplication()
{
    private val applicationInjector = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    @Inject
    protected lateinit var fabric: Fabric
    @Inject
    protected lateinit var timberTree: Timber.Tree

    init
    {
        instance = this
    }

    override fun onCreate()
    {
        super.onCreate()

        Timber.plant(timberTree)
        logKeyHash()

        // Launch screen timeout
        Thread.sleep(1000)
    }

    public override fun applicationInjector() = applicationInjector

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

    companion object
    {
        /**
         * Use injection instead, this is a requirement for Glide module.
         *
         * @hide
         */
        lateinit var instance: BeaverApplication
    }
}

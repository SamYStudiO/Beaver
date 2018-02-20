package net.samystudio.beaver

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import net.samystudio.beaver.di.component.DaggerApplicationComponent
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HighwayApplication : DaggerApplication()
{
    override fun onCreate()
    {
        super.onCreate()

        initFirebaseCrash()
        initTimber()
        logKeyHash()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
    {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    private fun initFirebaseCrash()
    {
        // TODO FirebaseCrash.setCrashCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initTimber()
    {
        Timber.plant(object : Timber.DebugTree()
                     {
                         override fun isLoggable(tag: String?, priority: Int): Boolean
                         {
                             return BuildConfig.DEBUG || priority >= Log.INFO
                         }
                     })
    }

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

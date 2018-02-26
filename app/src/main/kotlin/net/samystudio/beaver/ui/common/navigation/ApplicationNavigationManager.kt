@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.base.activity.BaseActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ApplicationNavigationManager @Inject constructor(@param:ApplicationContext
                                                            protected val context: Context)
{
    fun startActivity(activityClass: Class<out BaseActivity>,
                      extras: Bundle? = null,
                      options: Bundle? = null)
    {
        val intent = Intent(context, activityClass)

        if (extras != null)
            intent.putExtras(extras)

        context.startActivity(intent, options)
    }

    fun startActivity(intent: Intent,
                      options: Bundle? = null)
    {
        context.startActivity(intent, options)
    }

    fun startUrl(url: String) = startUrl(Uri.parse(url))
    fun startUrl(uri: Uri) = context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}

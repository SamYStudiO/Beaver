@file:Suppress("unused")

package net.samystudio.beaver.ui.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import net.samystudio.beaver.di.qualifier.ApplicationContext
import net.samystudio.beaver.ui.base.activity.BaseActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor(@param:ApplicationContext private val context: Context)
{
    @JvmOverloads
    fun startActivity(fromActivity: FragmentActivity,
                      activityClass: Class<out BaseActivity>,
                      bundle: Bundle? = null)
    {
        val intent = Intent(context, activityClass)

        if (bundle != null)
            intent.putExtras(bundle)

        fromActivity.startActivity(intent)
        fromActivity.finish()
    }

    fun startUrl(fromActivity: FragmentActivity, url: String) =
            fromActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

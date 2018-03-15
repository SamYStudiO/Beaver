@file:Suppress("unused")

package net.samystudio.beaver.ui.common.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.activity.BaseActivity
import javax.inject.Inject

/**
 * Navigation helper to navigate through activities.
 */
@ActivityScope
open class ActivityNavigationManager
@Inject
constructor(protected val activity: AppCompatActivity) :
    ApplicationNavigationManager(activity)
{
    fun startActivity(activityClass: Class<out BaseActivity<*>>,
                      extras: Bundle? = null,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        startActivity(
            NavigationRequest.ActivityRequest(activityClass, extras, options, forResultRequestCode,
                                              finishCurrentActivity))

    fun startActivity(intent: Intent,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        startActivity(NavigationRequest.ActivityRequest(intent, options, forResultRequestCode,
                                                        finishCurrentActivity))

    @SuppressLint("RestrictedApi")
    fun startActivity(
        activityRequest: NavigationRequest.ActivityRequest): NavigationRequest.ActivityRequest
    {
        val intent = activityRequest.intent(activity)

        activityRequest.extras?.let { intent.putExtras(it) }

        if (activityRequest.forResultRequestCode != null)
            activity.startActivityForResult(intent, activityRequest.forResultRequestCode,
                                            activityRequest.options)
        else activity.startActivity(intent, activityRequest.options)

        if (activityRequest.finishCurrentActivity) activity.finish()

        return activityRequest
    }
}

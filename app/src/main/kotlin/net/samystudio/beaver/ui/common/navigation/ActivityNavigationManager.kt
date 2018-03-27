@file:Suppress("unused")

package net.samystudio.beaver.ui.common.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.activity.BaseActivity
import javax.inject.Inject

/**
 * Navigation helper to navigate through activities from an activity.
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
        startActivity(ActivityRequest(activityClass, extras, options, forResultRequestCode,
                                      finishCurrentActivity))

    fun startActivity(intent: Intent,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        startActivity(ActivityRequest(intent, options, forResultRequestCode, finishCurrentActivity))

    @SuppressLint("RestrictedApi")
    fun startActivity(activityRequest: ActivityRequest): ActivityRequest
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

    /**
     * Advanced [android.app.Activity] request to use along with [ActivityNavigationManager.startActivity].
     */
    class ActivityRequest
    private constructor(private val activityClass: Class<out BaseActivity<*>>? = null,
                        private val intent: Intent? = null, val extras: Bundle? = null,
                        val options: Bundle? = null, val forResultRequestCode: Int? = null,
                        val finishCurrentActivity: Boolean = false)
    {
        constructor(activityClass: Class<out BaseActivity<*>>, extras: Bundle? = null,
                    options: Bundle? = null, forResultRequestCode: Int? = null,
                    finishCurrentActivity: Boolean = false) :
                this(activityClass, null, extras, options, forResultRequestCode,
                     finishCurrentActivity)

        constructor(intent: Intent, options: Bundle? = null, forResultRequestCode: Int? = null,
                    finishCurrentActivity: Boolean = false) :
                this(null, intent, null, options, forResultRequestCode, finishCurrentActivity)

        fun intent(context: Context) = intent ?: Intent(context, activityClass)
    }
}



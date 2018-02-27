@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.common.navigation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.samystudio.beaver.di.scope.ActivityScope
import net.samystudio.beaver.ui.base.activity.BaseActivity
import javax.inject.Inject

@ActivityScope
open class ActivityNavigationManager @Inject constructor(protected val activity: AppCompatActivity) :
    ApplicationNavigationManager(activity)
{
    fun startActivity(activityClass: Class<out BaseActivity<*>>,
                      extras: Bundle? = null,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false)
    {
        val intent = Intent(activity, activityClass)

        if (extras != null)
            intent.putExtras(extras)

        if (forResultRequestCode != null) activity.startActivityForResult(intent,
                                                                          forResultRequestCode/*,
                                                                          options*/)
        else activity.startActivity(intent, options)

        if (finishCurrentActivity) activity.finish()
    }

    fun startActivity(intent: Intent,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false)
    {
        if (forResultRequestCode != null) activity.startActivityForResult(intent,
                                                                          forResultRequestCode/*,
                                                                          options*/)
        activity.startActivity(intent, options)

        if (finishCurrentActivity) activity.finish()
    }
}

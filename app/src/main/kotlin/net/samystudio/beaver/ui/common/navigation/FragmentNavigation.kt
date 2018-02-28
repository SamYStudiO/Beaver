package net.samystudio.beaver.ui.common.navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IntRange
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.fragment.dialog.BaseDialog

interface FragmentNavigation
{
    val fragmentNavigationManager: FragmentNavigationManager

    fun <T : BaseFragment<*>> getCurrentFragment(): T? =
        fragmentNavigationManager.getCurrentFragment()

    /**
     * @see ActivityNavigationManager.startActivity
     */
    fun startActivity(activityClass: Class<out BaseActivity<*>>,
                      extras: Bundle? = null,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        fragmentNavigationManager.startActivity(activityClass, extras,
                                                options,
                                                forResultRequestCode,
                                                finishCurrentActivity)

    /**
     * @see ActivityNavigationManager.startActivity
     */
    fun startActivity(intent: Intent,
                      options: Bundle? = null,
                      forResultRequestCode: Int? = null,
                      finishCurrentActivity: Boolean = false) =
        fragmentNavigationManager.startActivity(intent,
                                                options,
                                                forResultRequestCode,
                                                finishCurrentActivity)

    /**
     * @see ActivityNavigationManager.startUrl
     */
    fun startUrl(url: String) = startUrl(Uri.parse(url))

    /**
     * @see ActivityNavigationManager.startUrl
     */
    fun startUrl(uri: Uri) = fragmentNavigationManager.startUrl(uri)

    fun onNewUrl(uri: Uri)
    {
    }

    /**
     * @see FragmentNavigationManager.startFragment
     */
    fun <T : BaseFragment<*>> startFragment(fragment: T,
                                            bundle: Bundle? = null,
                                            addToBackStack: Boolean = true,
                                            forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragment,
                                                bundle,
                                                addToBackStack,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.startFragment
     */
    fun <T : BaseFragment<*>> startFragment(fragmentClass: Class<T>,
                                            bundle: Bundle? = null,
                                            addToBackStack: Boolean = true,
                                            forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragmentClass,
                                                bundle,
                                                addToBackStack,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.startFragment
     */
    fun <T : BaseFragment<*>> startFragment(fragmentNavigationRequest: FragmentNavigationRequest<T>,
                                            forResultRequestCode: Int? = null) =
        fragmentNavigationManager.startFragment(fragmentNavigationRequest,
                                                forResultRequestCode)

    /**
     * @see FragmentNavigationManager.clearBackStack
     */
    fun clearBackStack(@FragmentNavigationManager.StateLossPolicy
                       stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.clearBackStack(stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    fun popBackStack(@FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    fun popBackStack(tag: String,
                     flags: Int,
                     @FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(tag, flags, stateLossPolicy)

    /**
     * @see FragmentNavigationManager.popBackStack
     */
    fun popBackStack(@IntRange(from = 1) offset: Int,
                     @FragmentNavigationManager.StateLossPolicy
                     stateLossPolicy: Long = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.popBackStack(offset, stateLossPolicy)

    /**
     * @see FragmentNavigationManager.dismissDialog
     */
    fun dismissDialog(dialog: BaseDialog<*>,
                      @FragmentNavigationManager.StateLossPolicy
                      stateLossPolicy: Long? = fragmentNavigationManager.defaultStateLossPolicy) =
        fragmentNavigationManager.dismissDialog(dialog, stateLossPolicy)

    /**
     * @see FragmentNavigationManager.dismissDialog
     */
    fun dismissDialog(tag: String, @FragmentNavigationManager.StateLossPolicy
    stateLossPolicy: Long? = fragmentNavigationManager.defaultStateLossPolicy): Boolean =
        fragmentNavigationManager.dismissDialog(tag, stateLossPolicy)
}
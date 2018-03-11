@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import com.google.firebase.analytics.FirebaseAnalytics
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.activity.BaseActivity
import net.samystudio.beaver.ui.base.dialog.DialogListener
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager

/**
 * Simple fragment with no injection or view model to avoid boilerplate with screen/dialog that
 * display really simple static content. If you want injection or view model, base class is
 * [BaseFragment].
 */
abstract class BaseSimpleFragment : AppCompatDialogFragment(),
                                    DialogInterface.OnShowListener
{
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected open lateinit var fragmentNavigationManager: FragmentNavigationManager
    protected open lateinit var childFragmentNavigationManager: FragmentNavigationManager
    protected open lateinit var firebaseAnalytics: FirebaseAnalytics
    protected var viewDestroyed: Boolean = false
    @State
    private var resultCode: Int = Activity.RESULT_CANCELED
    @State
    private var resultIntent: Intent? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewDestroyed = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        // In most case we'll extends BaseDataFragment and get these initialized with injection, but
        // if we want really simple fragment screen with no data (less boilerplate), we have to
        // initialize these manually.
        if (!::fragmentNavigationManager.isInitialized)
        {
            fragmentNavigationManager = (activity as? BaseActivity<*>)?.fragmentNavigationManager ?:
                    FragmentNavigationManager(activity as AppCompatActivity,
                                              fragmentManager!!,
                                              R.id.fragment_container)
        }

        if (!::childFragmentNavigationManager.isInitialized)
        {
            childFragmentNavigationManager =
                    FragmentNavigationManager(activity as AppCompatActivity,
                                              childFragmentManager,
                                              R.id.fragment_container)
        }

        if (!::firebaseAnalytics.isInitialized)
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        if (showsDialog) dialog.setOnShowListener(this)
    }

    override fun onResume()
    {
        super.onResume()

        firebaseAnalytics.setCurrentScreen(activity!!, javaClass.simpleName, javaClass.simpleName)
    }

    /**
     * Parent [Activity] wil call this when [Activity.onBackPressed] is called to check if we want
     * to handle this event. Returning true will cancel activity default behaviour, returning false
     * will let [Activity] handle this event.
     * Note: will not be called if [getShowsDialog] is true.
     */
    open fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return false
    }

    /**
     * Get if specified item will be consume from [onOptionsItemSelected], no more action is
     * required here, you'll consume action in [onOptionsItemSelected] and should return the same
     * [Boolean] from both method with the same item.
     * Note: will not be called if [getShowsDialog] is true.
     */
    open fun willConsumeOptionsItem(item: MenuItem): Boolean
    {
        return false
    }

    override fun onShow(dialog: DialogInterface?)
    {
        activity?.let {
            if (it is DialogListener)
                it.onDialogShow(targetRequestCode)
        }

        targetFragment?.let {
            if (it is DialogListener)
                it.onDialogShow(targetRequestCode)
        }
    }

    override fun onCancel(dialog: DialogInterface?)
    {
        super.onCancel(dialog)

        activity?.let {
            if (it is DialogListener)
                it.onDialogCancel(targetRequestCode)
        }

        targetFragment?.let {
            if (it is DialogListener)
                it.onDialogCancel(targetRequestCode)
        }

        setResult(Activity.RESULT_CANCELED, null)
        finish()
    }

    override fun onDismiss(dialog: DialogInterface?)
    {
        super.onDismiss(dialog)

        if (!viewDestroyed)
        {
            activity?.let {
                if (it is DialogListener)
                    it.onDialogDismiss(targetRequestCode)
            }

            targetFragment?.let {
                if (it is DialogListener)
                    it.onDialogDismiss(targetRequestCode)
            }
        }
    }

    /**
     * @see [android.app.Activity.setResult]
     */
    fun setResult(code: Int, intent: Intent?)
    {
        resultCode = code
        resultIntent = intent
    }

    /**
     * Same as [android.app.Activity.finish], if [BaseSimpleFragment] is a dialog it will be dismissed
     * otherwise [android.support.v4.app.FragmentManager] stack will pop. Don't use dismiss or
     * popBackStack manually if you want to trigger result to target [BaseSimpleFragment].
     */
    open fun finish()
    {
        targetFragment?.onActivityResult(targetRequestCode, resultCode, resultIntent)

        if (showsDialog) dismiss()
        else fragmentNavigationManager.popBackStack()
    }

    override fun onDestroy()
    {
        super.onDestroy()

        viewDestroyed = true
    }
}

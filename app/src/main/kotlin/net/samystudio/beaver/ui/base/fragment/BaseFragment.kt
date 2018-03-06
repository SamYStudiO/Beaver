@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import dagger.android.support.DaggerAppCompatDialogFragment
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.ui.base.activity.BaseActionBarActivity
import net.samystudio.beaver.ui.base.dialog.DialogListener
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseFragment<VM : BaseFragmentViewModel> : DaggerAppCompatDialogFragment()
{
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @Inject
    @field:FragmentLevel
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    protected var viewDestroyed: Boolean = false
    private var savedInstanceState: Bundle? = null
    @State
    private var resultCode: Int = Activity.RESULT_CANCELED
    @State
    private var resultIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState
    }

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

        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.titleObservable.observe(this, Observer {
            if (showsDialog) dialog.setTitle(it)
            else
            {
                if (activity is BaseActionBarActivity<*>)
                    (activity as BaseActionBarActivity<*>).animatedTitle = it
                else
                    activity?.title = it
            }
        })
        viewModel.resultObservable.observe(this, Observer {
            it?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })

        onViewModelCreated(savedInstanceState)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleState(activity!!.intent, savedInstanceState, arguments)
        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

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
     */
    open fun willConsumeOptionsItem(item: MenuItem): Boolean
    {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        viewModel.handleActivityResult(requestCode, requestCode, data)
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
     * Same as [android.app.Activity.finish], if [BaseFragment] is a dialog it will be dismissed
     * otherwise [android.support.v4.app.FragmentManager] stack will pop. Don't use dismiss or
     * popBackStack manually if you want to trigger result to target [BaseFragment].
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

    override fun onDismiss(dialog: DialogInterface?)
    {
        super.onDismiss(dialog)

        if (!viewDestroyed)
        {
            activity?.let {
                if (it is DialogListener)
                    it.onDismissDialog(targetRequestCode)
            }

            targetFragment?.let {
                if (it is DialogListener)
                    it.onDismissDialog(targetRequestCode)
            }
        }
    }

    override fun onCancel(dialog: DialogInterface?)
    {
        super.onCancel(dialog)

        activity?.let {
            if (it is DialogListener)
                it.onCancelDialog(targetRequestCode)
        }

        targetFragment?.let {
            if (it is DialogListener)
                it.onCancelDialog(targetRequestCode)
        }

        setResult(Activity.RESULT_CANCELED, null)
        finish()
    }
}

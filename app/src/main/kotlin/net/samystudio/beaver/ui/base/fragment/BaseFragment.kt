@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evernote.android.state.State
import dagger.android.support.DaggerFragment
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.ui.base.activity.BaseActionBarActivity
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

abstract class BaseFragment<VM : BaseFragmentViewModel> : DaggerFragment()
{
    @Inject
    @field:FragmentLevel
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected val viewModelIsInitialized
        get() = ::viewModel.isInitialized
    lateinit var viewModel: VM
    private var lastIntent: Intent? = null

    @State
    var title: String = ""
        set(value)
        {
            field = value

            if (activity is BaseActionBarActivity<*>)
                (activity as BaseActionBarActivity<*>).toggleTitle(value)
        }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)

        onViewModelCreated(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()

        val intent = activity?.intent

        if (intent != null)
        {
            if (!intent.filterEquals(lastIntent))
                viewModel.handleIntent(intent)

            if (intent.extras != null)
                viewModel.handleExtras(intent.extras)

            lastIntent = intent
        }
    }

    /**
     * @hide
     */
    fun onBackPressed(): Boolean
    {
        setResult(Activity.RESULT_CANCELED, null)

        return consumeOnBackPressed()
    }

    /**
     * Override this to catch back key pressed.
     *
     * @return true if you consume event, this means no more parent will catch this event, false
     * if you want parent to handle this event.
     */
    open fun consumeOnBackPressed() = false

    @JvmOverloads
    fun setResult(code: Int, intent: Intent? = null)
    {
        val fragment = targetFragment as BaseFragment<*>?
        fragment?.onActivityResult(targetRequestCode, code, intent)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }
}

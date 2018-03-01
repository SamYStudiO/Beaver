@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.ui.base.activity.BaseActionBarActivity
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

abstract class BaseFragment<VM : BaseFragmentViewModel> : DaggerFragment()
{
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    @Inject
    @field:FragmentLevel
    protected lateinit var viewModelProvider: ViewModelProvider

    protected abstract val viewModelClass: Class<VM>
    protected val viewModelIsInitialized
        get() = ::viewModel.isInitialized
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null

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

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.titleObservable.observe(this, Observer {
            if (activity is BaseActionBarActivity<*>)
                (activity as BaseActionBarActivity<*>).animatedTitle = it
            else
                activity?.title = it
        })

        onViewModelCreated(savedInstanceState)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleRestoreState(activity!!.intent, savedInstanceState, arguments)
        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

    fun onBackPressed(): Boolean
    {
        return viewModel.handleBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return viewModel.handleOptionsItemSelected(item)
    }

    fun willConsumeOptionsItem(item: MenuItem): Boolean
    {
        return viewModel.willConsumeOptionsItem(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        viewModel.handleActivityResult(requestCode, requestCode, data)
    }
}

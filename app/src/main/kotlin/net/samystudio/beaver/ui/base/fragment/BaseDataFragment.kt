@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import net.samystudio.beaver.di.qualifier.FragmentLevel
import net.samystudio.beaver.ui.base.activity.BaseActionBarActivity
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseDataFragment<VM : BaseFragmentViewModel> : BaseFragment(),
                                                              HasSupportFragmentInjector,
                                                              DialogInterface.OnShowListener
{
    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    final override lateinit var fragmentNavigationManager: FragmentNavigationManager
    @Inject
    @field:FragmentLevel
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM

    override fun supportFragmentInjector(): AndroidInjector<Fragment>?
    {
        return childFragmentInjector
    }

    override fun onAttach(context: Context?)
    {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
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

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        viewModel.handleActivityResult(requestCode, requestCode, data)
    }
}

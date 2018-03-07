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
import net.samystudio.beaver.di.qualifier.FragmentContext
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
    @field:FragmentContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null
    private var requestCode: Int? = null
    private var resultCode: Int? = null
    private var resultData: Intent? = null

    override fun supportFragmentInjector(): AndroidInjector<Fragment>?
    {
        return childFragmentInjector
    }

    override fun onAttach(context: Context?)
    {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState
        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.handleCreate()
        onViewModelCreated(savedInstanceState)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        viewModel.resultObservable.observe(this, Observer {
            it?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        this.requestCode = requestCode
        this.resultCode = resultCode
        this.resultData = data
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleState(arguments, savedInstanceState, requestCode, resultCode, resultData)
        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }
}

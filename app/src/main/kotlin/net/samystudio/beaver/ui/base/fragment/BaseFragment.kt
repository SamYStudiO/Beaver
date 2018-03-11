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
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseFragment<VM : BaseFragmentViewModel> : BaseSimpleFragment(),
                                                          HasSupportFragmentInjector,
                                                          DialogInterface.OnShowListener
{
    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    @field:ActivityContext
    final override lateinit var fragmentNavigationManager: FragmentNavigationManager
    @Inject
    @field:FragmentContext
    final override lateinit var childFragmentNavigationManager: FragmentNavigationManager
    @Inject
    final override lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject
    @field:FragmentContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null
    private var resultRequestCode: Int? = null
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

    @CallSuper
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
        this.resultRequestCode = requestCode
        this.resultCode = resultCode
        this.resultData = data
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleState(arguments,
                              savedInstanceState,
                              resultRequestCode,
                              resultCode,
                              resultData)
        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }
}

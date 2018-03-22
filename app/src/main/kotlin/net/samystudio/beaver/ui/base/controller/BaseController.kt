package net.samystudio.beaver.ui.base.controller

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStore
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.ivianuu.contributer.conductor.ConductorInjection
import net.samystudio.beaver.ui.base.viewmodel.BaseControllerViewModel
import javax.inject.Inject

abstract class BaseController<VM : BaseControllerViewModel> : BaseSimpleController()
{
    @Inject
    final override lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject
    protected lateinit var viewModelStore: ViewModelStore
    @Inject
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null

    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)

        this.savedInstanceState = savedInstanceState
    }

    @CallSuper
    override fun onContextAvailable(context: Context)
    {
        super.onContextAvailable(context)

        if (!::viewModel.isInitialized)
        {
            ConductorInjection.inject(this)
            viewModel = viewModelProvider.get(viewModelClass)
            viewModel.handleCreate()
            activity?.intent?.let { viewModel.handleIntent(it) }
            viewModel.handleArguments(args)

            onViewModelCreated()
        }

        savedInstanceState?.let {
            viewModel.handleRestoreInstanceState(it)
            savedInstanceState = null
        }
    }

    @CallSuper
    open fun onNewIntent(intent: Intent)
    {
        viewModel.handleIntent(intent)

        childRouters.forEach { router ->
            router.backstack.forEach {
                (it.controller() as? BaseController<*>)?.onNewIntent(intent)
            }
        }
    }

    @CallSuper
    protected open fun onViewModelCreated()
    {
        viewModel.resultEvent.observe(this, Observer {
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
        viewModel.handleResult(requestCode, resultCode, data)
    }

    override fun onAttach(view: View)
    {
        super.onAttach(view)

        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

    override fun onDestroy()
    {
        super.onDestroy()

        viewModelStore.clear()
    }
}
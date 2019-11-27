package net.samystudio.beaver.ui.base.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject
import androidx.fragment.app.activityViewModels as activityViewModelsInternal
import androidx.fragment.app.viewModels as viewModelsInternal

abstract class BaseViewModelFragment<VM : BaseFragmentViewModel> : BaseDaggerFragment(),
    DialogInterface.OnShowListener {
    /**
     * @see BaseViewModelFragmentModule.bindViewModelFactory
     */
    @Inject
    @field:FragmentContext
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    @field:ActivityContext
    protected lateinit var activityViewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.handleCreate(savedInstanceState)
        activity?.intent?.let { viewModel.handleIntent(it) }
        arguments?.let { viewModel.handleArguments(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationCommand.observe(
            viewLifecycleOwner,
            Observer { request ->
                request?.let {
                    navController.navigate(it, activity)
                }
            })

        viewModel.resultEvent.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.handleIntent(intent)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.handleResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.handleSaveInstanceState(outState)
    }

    fun onTrimMemory(level: Int) {
        viewModel.handleTrimMemory(level)
    }

    @Suppress("UNUSED_PARAMETER")
    protected inline fun <reified VM : ViewModel> viewModels(
        ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModelsInternal<VM> { viewModelFactory }

    protected inline fun <reified VM : ViewModel> activityViewModels() =
        activityViewModelsInternal<VM> { activityViewModelFactory }
}

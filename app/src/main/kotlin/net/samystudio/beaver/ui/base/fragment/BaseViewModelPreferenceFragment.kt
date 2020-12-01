package net.samystudio.beaver.ui.base.fragment

import android.content.ComponentCallbacks2
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel

abstract class BaseViewModelPreferenceFragment<VM : BaseFragmentViewModel> :
    BasePreferenceFragment(), ComponentCallbacks2 {
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
            { request ->
                request?.let {
                    navController.navigate(it, activity)
                }
            })

        viewModel.resultEvent.observe(viewLifecycleOwner, { result ->
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

    override fun onTrimMemory(level: Int) {
        viewModel.handleTrimMemory(level)
    }
}

package net.samystudio.beaver.ui.base.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

abstract class BaseViewModelFragment<VM : BaseFragmentViewModel> : BaseDaggerFragment(),
    DialogInterface.OnShowListener {
    /**
     * @see [net.samystudio.beaver.di.module.FirebaseModule.provideFirebaseAnalytics]
     */
    @Inject
    final override lateinit var firebaseAnalytics: FirebaseAnalytics
    /**
     * @see [net.samystudio.beaver.ui.base.fragment.BaseFragmentModule.provideViewModelProvider]
     */
    @Inject
    @field:FragmentContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.handleCreate()
        activity?.intent?.let { viewModel.handleIntent(it) }
        arguments?.let { viewModel.handleArguments(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigationCommand.observe(
            viewLifecycleOwner,
            Observer { request ->
                request?.let {
                    navController.navigate(it, activity, fragmentManager)
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let { viewModel.handleRestoreInstanceState(it) }
    }

    override fun onNewIntent(intent: Intent) {
        viewModel.handleIntent(intent)

        super.onNewIntent(intent)
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
}

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import net.samystudio.beaver.di.qualifier.FragmentContext
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

abstract class BaseViewModelFragment<VM : BaseFragmentViewModel> : BaseFragment(),
    HasSupportFragmentInjector, DialogInterface.OnShowListener {
    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    final override lateinit var firebaseAnalytics: FirebaseAnalytics
    @Inject
    final override lateinit var navigationController: NavController
    @Inject
    @field:FragmentContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
            Observer { request -> request?.let { handleNavigationRequest(it, fragmentManager!!) } })

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

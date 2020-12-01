@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.content.ComponentCallbacks2
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.reactivex.rxjava3.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.base.fragment.BaseViewModelPreferenceFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel

abstract class BaseActivity<VM : BaseActivityViewModel> : AppCompatActivity() {
    private val savable = Bundle()
    protected abstract val navControllerId: Int
    protected val navController: NavController
        get() = findNavController(navControllerId)
    protected var destroyDisposable: CompositeDisposable? = null
    protected var stopDisposable: CompositeDisposable? = null
    protected var pauseDisposable: CompositeDisposable? = null
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            savable.putAll(savedInstanceState.getBundle(getClassTag()))

        destroyDisposable = CompositeDisposable()
        viewModel.handleCreate(savedInstanceState)
        viewModel.handleIntent(intent)
        onViewModelCreated()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        viewModel.handleIntent(intent)

        supportFragmentManager.fragments.forEach {
            (it as? BaseViewModelFragment<*, *>)?.onNewIntent(intent)
            (it as? BaseViewModelPreferenceFragment<*>)?.onNewIntent(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        stopDisposable = CompositeDisposable()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        pauseDisposable = CompositeDisposable()
        viewModel.handleReady()
    }

    override fun onPause() {
        super.onPause()
        pauseDisposable?.dispose()
    }

    override fun onStop() {
        super.onStop()
        stopDisposable?.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle(getClassTag(), savable)
        viewModel.handleSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable?.dispose()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        viewModel.handleTrimMemory(level)
        supportFragmentManager.fragments.forEach {
            (it as? ComponentCallbacks2)?.onTrimMemory(level)
        }
    }

    @CallSuper
    protected open fun onViewModelCreated() {
        viewModel.navigationCommand.observe(this,
            { request ->
                request?.let {
                    navController.navigate(it, this)
                }
            })
        viewModel.resultEvent.observe(this, { result ->
            result?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
    }

    protected fun <T> state(
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.Nullable(savable, beforeSetCallback, afterSetCallback)

    protected fun <T> state(
        defaultValue: T,
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.NotNull(savable, defaultValue, beforeSetCallback, afterSetCallback)
}
@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject
import androidx.activity.viewModels as viewModelsInternal

abstract class BaseActivity<VM : BaseActivityViewModel> : AppCompatActivity(),
    HasAndroidInjector {
    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any>
    /**
     * @see [net.samystudio.beaver.ui.base.activity.BaseActivityModule.bindViewModelFactory]
     */
    @Inject
    @field:ActivityContext
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    abstract val viewModel: VM
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val navController: NavController
    protected var destroyDisposable: CompositeDisposable? = null
    protected var stopDisposable: CompositeDisposable? = null
    protected var pauseDisposable: CompositeDisposable? = null
    @Inject
    protected lateinit var userManager: UserManager
    private val savable = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutViewRes)

        destroyDisposable = CompositeDisposable()
        viewModel.handleCreate()
        viewModel.handleIntent(intent)
        onViewModelCreated()
    }

    @CallSuper
    protected open fun onViewModelCreated() {
        viewModel.navigationCommand.observe(this,
            Observer { request ->
                request?.let {
                    navController.navigate(it, this, supportFragmentManager)
                }
            })
        viewModel.resultEvent.observe(this, Observer
        { result ->
            result?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)
        viewModel.handleIntent(intent)

        supportFragmentManager.fragments.forEach {
            (it as? BaseViewModelFragment<*>)?.onNewIntent(
                intent
            )
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.handleResult(requestCode, resultCode, data)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savable.putAll(savedInstanceState.getBundle(getClassTag()))
        viewModel.handleRestoreInstanceState(savedInstanceState)

        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        stopDisposable = CompositeDisposable()
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

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable?.dispose()
    }

    @Suppress("UNUSED_PARAMETER")
    protected inline fun <reified VM : ViewModel> viewModels(
        ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModelsInternal<VM> { viewModelFactory }

    protected fun <T> state(setterCallback: (value: T) -> Unit) =
        InstanceStateProvider.Nullable(savable, setterCallback)

    protected fun <T> state(defaultValue: T, setterCallback: (value: T) -> Unit) =
        InstanceStateProvider.NotNull(savable, defaultValue, setterCallback)
}
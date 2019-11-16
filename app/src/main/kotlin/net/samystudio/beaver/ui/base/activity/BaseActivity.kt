@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.disposables.CompositeDisposable
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ext.hideKeyboard
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject
import androidx.activity.viewModels as viewModelsInternal

abstract class BaseActivity<VM : BaseActivityViewModel> : AppCompatActivity(),
    HasAndroidInjector {
    private val savable = Bundle()
    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any>
    /**
     * @see BaseActivityModule.bindViewModelFactory
     */
    @Inject
    @field:ActivityContext
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val navControllerId: Int
    protected val navController: NavController
        get() = findNavController(navControllerId)
    protected var destroyDisposable: CompositeDisposable? = null
    protected var stopDisposable: CompositeDisposable? = null
    protected var pauseDisposable: CompositeDisposable? = null
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null)
            savable.putAll(savedInstanceState.getBundle(getClassTag()))

        setContentView(layoutViewRes)

        destroyDisposable = CompositeDisposable()
        viewModel.handleCreate(savedInstanceState)
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    hideKeyboard(v)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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
            (it as? BaseViewModelFragment<*>)?.onTrimMemory(
                level
            )
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    @Suppress("UNUSED_PARAMETER")
    protected inline fun <reified VM : ViewModel> viewModels(
        ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = viewModelsInternal<VM> { viewModelFactory }

    protected fun <T> state(setterCallback: ((value: T) -> Unit)? = null) =
        InstanceStateProvider.Nullable(savable, setterCallback)

    protected fun <T> state(defaultValue: T, setterCallback: ((value: T) -> Unit)? = null) =
        InstanceStateProvider.NotNull(savable, defaultValue, setterCallback)
}
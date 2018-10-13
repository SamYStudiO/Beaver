package net.samystudio.beaver.ui.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ext.navigate
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject

abstract class BaseActivity<VM : BaseActivityViewModel> : AppCompatActivity(),
    HasSupportFragmentInjector {
    @Inject
    protected lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val navController: NavController
    /**
     * @see [net.samystudio.beaver.ui.base.activity.BaseActivityModule.provideViewModelProvider]
     */
    @Inject
    @field:ActivityContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    @Inject
    protected lateinit var userManager: UserManager


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutViewRes)

        viewModel = viewModelProvider.get(viewModelClass)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null)
            viewModel.handleRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        viewModel.handleReady()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return supportFragmentInjector
    }
}
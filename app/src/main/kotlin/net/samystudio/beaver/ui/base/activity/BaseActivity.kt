@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.app.FragmentManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ui.base.fragment.BaseSimpleFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity<VM : BaseActivityViewModel> : DaggerAppCompatActivity(),
                                                          FragmentManager.OnBackStackChangedListener
{
    abstract val defaultFragmentClass: Class<out BaseSimpleFragment>
    abstract val defaultFragmentBundle: Bundle?
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    @Inject
    @field:ActivityContext
    lateinit var fragmentNavigationManager: FragmentNavigationManager
    @Inject
    @field:ActivityContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null
    private var resultRequestCode: Int? = null
    private var resultCode: Int? = null
    private var resultData: Intent? = null
    private var resumed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(layoutViewRes)
        fragmentManager.addOnBackStackChangedListener(this)
        this.savedInstanceState = savedInstanceState

        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.handleCreate()
        onViewModelCreated(savedInstanceState)
    }

    @CallSuper
    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
        viewModel.titleObservable.observe(this, Observer { it -> title = it })
        viewModel.resultCommand.observe(this, Observer {
            it?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)

        setIntent(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        this.resultRequestCode = requestCode
        this.resultCode = resultCode
        this.resultData = data

        // This is a rare case only happening due to our way of handling result from dialogs using
        // onActivityResult, plus we're using DialogFragment so activity will not pause/resume when
        // opening one. Anyway we need to notify view model if we already resumed.
        if (resumed) handleResume()
    }

    override fun onResume()
    {
        super.onResume()

        resumed = true

        handleResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val currentFragment: BaseSimpleFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.willConsumeOptionsItem(item))
        {
            when (item.itemId)
            {
                android.R.id.home ->
                    if (fragmentNavigationManager.clearBackStack()) return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

    override fun onBackPressed()
    {
        val currentFragment: BaseSimpleFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    override fun onBackStackChanged()
    {
        val currentFragment: BaseSimpleFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null)
            fragmentNavigationManager.startFragment(defaultFragmentClass,
                                                    defaultFragmentBundle,
                                                    false)
    }

    override fun onPause()
    {
        super.onPause()

        resumed = false
    }

    private fun handleResume()
    {
        viewModel.handleState(intent, savedInstanceState, resultRequestCode, resultCode, resultData)
        viewModel.handleReady()
        this.resultRequestCode = null
        this.resultCode = null
        this.resultData = null
    }
}

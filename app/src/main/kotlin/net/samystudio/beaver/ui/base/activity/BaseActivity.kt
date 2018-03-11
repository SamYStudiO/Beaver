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
        viewModel.resultObservable.observe(this, Observer {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        this.resultRequestCode = requestCode
        this.resultCode = resultCode
        this.resultData = data
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleState(intent, savedInstanceState, resultRequestCode, resultCode, resultData)
        viewModel.handleReady()
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
}

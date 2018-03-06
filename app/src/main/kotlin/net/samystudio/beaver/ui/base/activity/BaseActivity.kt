@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.app.FragmentManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.di.qualifier.ActivityContext
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity<VM : BaseActivityViewModel> : DaggerAppCompatActivity(),
                                                          FragmentManager.OnBackStackChangedListener
{
    abstract val defaultFragmentClass: Class<out BaseFragment>
    abstract val defaultFragmentBundle: Bundle?
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    @Inject
    lateinit var fragmentNavigationManager: FragmentNavigationManager
    @Inject
    @field:ActivityContext
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState
        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.resultObservable.observe(this, Observer {
            it?.let {
                setResult(it.code, it.intent)
                if (it.finish)
                    finish()
            }
        })
        setContentView(layoutViewRes)
        fragmentManager.addOnBackStackChangedListener(this)
        onViewModelCreated(savedInstanceState)
    }

    protected open fun onViewModelCreated(savedInstanceState: Bundle?)
    {
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)

        setIntent(intent)
    }

    override fun onResume()
    {
        super.onResume()

        viewModel.handleState(intent, savedInstanceState)
        viewModel.handleReady()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

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
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onBackStackChanged()
    {
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null)
            fragmentNavigationManager.startFragment(defaultFragmentClass,
                                                    defaultFragmentBundle,
                                                    false)
    }
}

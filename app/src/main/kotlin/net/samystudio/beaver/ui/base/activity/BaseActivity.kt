@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.di.qualifier.ActivityLevel
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity<VM : BaseActivityViewModel> : DaggerAppCompatActivity()
{
    @Inject
    protected lateinit var fragmentManager: FragmentManager
    /**
     * @hide
     */
    @Inject
    override lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val defaultFragmentClass: Class<out BaseFragment<*>>
    @Inject
    @field:ActivityLevel
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var lastIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider.get(viewModelClass)
        setContentView(layoutViewRes)
        onViewModelCreated(savedInstanceState)

        // don't need to instantiate fragment when restoring state
        if (savedInstanceState == null)
            startFragment(defaultFragmentClass, null, false)
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

        if (!intent.filterEquals(lastIntent))
        {
            val i = Intent(intent)
            i.extras.clear() // we'll handle extras later
            viewModel.handleIntent(i)
        }

        if (intent.extras != null)
            viewModel.handleExtras(intent.extras)

        lastIntent = intent
    }

    override fun onBackPressed()
    {
        if (!viewModel.handleBackPressed())
            super.onBackPressed()
    }
}

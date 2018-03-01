@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.Menu
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import net.samystudio.beaver.di.qualifier.ActivityLevel
import net.samystudio.beaver.ui.base.viewmodel.BaseActivityViewModel
import javax.inject.Inject

abstract class BaseActivity<VM : BaseActivityViewModel> : DaggerAppCompatActivity()
{
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    @Inject
    @field:ActivityLevel
    protected lateinit var viewModelProvider: ViewModelProvider
    protected abstract val viewModelClass: Class<VM>
    lateinit var viewModel: VM
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState
        viewModel = viewModelProvider.get(viewModelClass)
        setContentView(layoutViewRes)
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

        viewModel.handleRestoreState(intent, savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean
    {
        viewModel.handleReady()

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (viewModel.handleOptionsItemSelected(item)) return true

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        viewModel.handleSaveInstanceState(outState)
    }

    override fun onBackPressed()
    {
        if (!viewModel.handleBackPressed())
            super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.handleActivityResult(requestCode, resultCode, data)
    }
}

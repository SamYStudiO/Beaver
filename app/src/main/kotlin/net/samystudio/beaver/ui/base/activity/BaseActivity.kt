@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import net.samystudio.beaver.ui.base.fragment.BaseFragment
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), FragmentManager.OnBackStackChangedListener
{
    @Inject
    protected lateinit var fragmentManager: FragmentManager
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    private var onPauseDisposables: CompositeDisposable? = null
    private var onStopDisposables: CompositeDisposable? = null
    private val onDestroyDisposables: CompositeDisposable = CompositeDisposable()

    override fun onBackPressed()
    {
        val currentFragment: BaseFragment? = fragmentNavigationManager.getCurrentFragment()

        if (currentFragment == null || !currentFragment.onBackPressed())
            super.onBackPressed()
    }

    final override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutViewRes())
        init(savedInstanceState)

        supportFragmentManager.addOnBackStackChangedListener(this)

        // don't need to instantiate fragment when restoring state
        if (savedInstanceState == null)
            fragmentNavigationManager.showFragment(getDefaultFragment())
    }

    override fun onStart()
    {
        super.onStart()

        onStopDisposables = CompositeDisposable()
    }

    override fun onResume()
    {
        super.onResume()

        onPauseDisposables = CompositeDisposable()
    }

    override fun onPause()
    {
        onPauseDisposables?.dispose()

        super.onPause()
    }

    override fun onStop()
    {
        onStopDisposables?.dispose()

        super.onStop()
    }

    override fun onDestroy()
    {
        onDestroyDisposables.dispose()

        super.onDestroy()
    }

    @CallSuper
    override fun onBackStackChanged()
    {
        if (fragmentManager.backStackEntryCount == 0) fragmentNavigationManager.showFragment(
                getDefaultFragment())
    }

    protected fun addOnPauseDisposable(disposable: Disposable)
    {
        if (onPauseDisposables == null || onPauseDisposables!!.isDisposed)
            throw IllegalStateException("Cannot add onPause disposable before onResume and after onPause")

        onPauseDisposables!!.add(disposable)
    }

    protected fun addOnStopDisposable(disposable: Disposable)
    {
        if (onStopDisposables == null || onStopDisposables!!.isDisposed)
            throw IllegalStateException("Cannot add onStop disposable before onStart and after onStop")

        onStopDisposables!!.add(disposable)
    }

    protected fun addOnDestroyDisposable(disposable: Disposable)
    {
        if (onDestroyDisposables.isDisposed)
            throw IllegalStateException("Cannot add onDestroy disposable after onDestroy")

        onDestroyDisposables.add(disposable)
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    @LayoutRes
    protected abstract fun getLayoutViewRes(): Int

    protected abstract fun getDefaultFragment(): Class<out BaseFragment>
}

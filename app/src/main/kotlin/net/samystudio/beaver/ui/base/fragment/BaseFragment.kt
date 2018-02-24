@file:Suppress("unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")

package net.samystudio.beaver.ui.base.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import net.samystudio.beaver.ui.base.dialog.DialogResult
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import net.samystudio.beaver.ui.common.navigation.NavigationManager
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), HasSupportFragmentInjector
{
    @Inject
    protected lateinit var navigationManager: NavigationManager
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val layoutViewRes: Int
    protected abstract val defaultTitle: String
    private var onPauseDisposables: CompositeDisposable? = null
    private var onStopDisposables: CompositeDisposable? = null
    private var onDestroyViewDisposables: CompositeDisposable? = null
    private val onDestroyDisposables: CompositeDisposable = CompositeDisposable()

    private val _titleObservable: BehaviorProcessor<String> = BehaviorProcessor.create()
    val titleObservable: Observable<String> = _titleObservable.toObservable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        _titleObservable.onNext(defaultTitle)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return if (layoutViewRes > 0) inflater.inflate(layoutViewRes, container, false)
        else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        onDestroyViewDisposables = CompositeDisposable()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        init(savedInstanceState)
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

    override fun onDestroyView()
    {
        onDestroyViewDisposables?.dispose()

        super.onDestroyView()
    }

    override fun onDestroy()
    {
        onDestroyDisposables.dispose()
        _titleObservable.onComplete()

        super.onDestroy()
    }

    protected fun addOnPauseDisposable(disposable: Disposable)
    {
        if (onPauseDisposables == null || onPauseDisposables!!.isDisposed)
            throw IllegalStateException(
                "Cannot add onPause disposable before onResume and after onPause")

        onPauseDisposables!!.add(disposable)
    }

    protected fun addOnStopDisposable(disposable: Disposable)
    {
        if (onStopDisposables == null || onStopDisposables!!.isDisposed)
            throw IllegalStateException(
                "Cannot add onStop disposable before onStart and after onStop")

        onStopDisposables!!.add(disposable)
    }

    protected fun addOnDestroyViewDisposable(disposable: Disposable)
    {
        if (onDestroyViewDisposables == null || onDestroyViewDisposables!!.isDisposed)
            throw IllegalStateException(
                "Cannot add onDestroyView disposable before onViewCreated and after onDestroyView")

        onDestroyViewDisposables!!.add(disposable)
    }

    protected fun addOnDestroyDisposable(disposable: Disposable)
    {
        if (onDestroyDisposables.isDisposed)
            throw IllegalStateException(
                "Cannot add onDestroy disposable after onDestroy")

        onDestroyDisposables.add(disposable)
    }

    /**
     * Override this to catch back key pressed.
     *
     * @return true if you consume event, this means no more parent will catch this event, false
     * if you want parent to handle this event.
     */
    fun onBackPressed() = false

    fun onDialogResult(requestCode: Int, result: DialogResult)
    {
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package net.samystudio.beaver.ui.base.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.Window
import dagger.android.support.DaggerAppCompatDialogFragment
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import net.samystudio.beaver.ui.common.navigation.FragmentNavigationManager
import javax.inject.Inject

// TODO results states (save/restore), DataDialog, DateResultDialog, AutoDispose
abstract class BaseDialog : DaggerAppCompatDialogFragment()
{
    @Inject
    protected lateinit var fragmentNavigationManager: FragmentNavigationManager
    @get:LayoutRes
    protected abstract val contentViewRes: Int
    private var onPauseDisposables: CompositeDisposable? = null
    private var onStopDisposables: CompositeDisposable? = null
    private var onDestroyViewDisposables: CompositeDisposable? = null
    private val onDestroyDisposables: CompositeDisposable = CompositeDisposable()
    private var lateShow: Boolean = false
    private var lateShowBundle: Bundle? = null

    private val _onDismissObservable = PublishSubject.create<DialogInterface>()
    val onDismissObservable: Completable = _onDismissObservable.ignoreElements()

    private val _onCancelObservable = PublishSubject.create<DialogInterface>()
    val onCancelObservable: Maybe<*> = _onCancelObservable.firstElement()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (lateShow) show(lateShowBundle)
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
        _onDismissObservable.onComplete()
        _onCancelObservable.onComplete()

        super.onDestroy()
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

    protected fun addOnDestroyViewDisposable(disposable: Disposable)
    {
        if (onDestroyViewDisposables == null || onDestroyViewDisposables!!.isDisposed)
            throw IllegalStateException("Cannot add onDestroyView disposable before onViewCreated and after onDestroyView")

        onDestroyViewDisposables!!.add(disposable)
    }

    protected fun addOnDestroyDisposable(disposable: Disposable)
    {
        if (onDestroyDisposables.isDisposed)
            throw IllegalStateException("Cannot add onDestroy disposable after onDestroy")

        onDestroyDisposables.add(disposable)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (dialog.window != null)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(LayoutInflater
                                  .from(context)
                                  .inflate(contentViewRes, null, false))

        onDestroyViewDisposables = CompositeDisposable()

        init(savedInstanceState)

        return dialog
    }

    final override fun show(fragmentManager: FragmentManager, tag: String)
    {
        throw IllegalArgumentException("Use show( Bundle? = null ) or FragmentNavigationManager.showFragment instead")
    }

    final override fun show(transaction: FragmentTransaction, tag: String): Int
    {
        throw IllegalArgumentException("Use show( Bundle? = null ) or FragmentNavigationManager.showFragment instead")
    }

    @JvmOverloads
    fun show(bundle: Bundle? = null)
    {
        if (::fragmentNavigationManager.isInitialized)
        {
            lateShow = false
            lateShowBundle = null
            fragmentNavigationManager.showFragment(this, bundle)
        }
        else
        {
            lateShow = true
            lateShowBundle = bundle
        }
    }

    override fun dismiss()
    {
        dismiss(null)
    }

    open fun dismiss(@FragmentNavigationManager.StateLossPolicy stateLossPolicy: Long?)
    {
        if (::fragmentNavigationManager.isInitialized)
            fragmentNavigationManager.dismissDialog(this, stateLossPolicy)
        else
        {
            lateShow = false
            lateShowBundle = null
        }
    }

    override fun dismissAllowingStateLoss()
    {
        throw IllegalArgumentException("Use dismiss instead")
    }

    override fun onDismiss(dialog: DialogInterface)
    {
        super.onDismiss(dialog)

        _onDismissObservable.onNext(dialog)
        _onDismissObservable.onComplete()
    }

    override fun onCancel(dialog: DialogInterface)
    {
        _onCancelObservable.onNext(dialog)
        _onCancelObservable.onComplete()
    }

    protected abstract fun init(savedInstanceState: Bundle?)
}

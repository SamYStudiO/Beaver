@file:Suppress("MemberVisibilityCanBePrivate")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import com.evernote.android.state.StateSaver
import io.reactivex.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel()
{
    @Inject
    protected lateinit var userManager: UserManager
    private val _resultEvent: SingleLiveEvent<Result> by lazy { SingleLiveEvent<Result>() }
    val resultEvent: LiveData<Result> = _resultEvent
    val userStatusObservable: LiveData<Boolean> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            userManager.statusObservable.toFlowable(BackpressureStrategy.LATEST))
    }

    open fun handleCreate()
    {
    }

    open fun handleIntent(intent: Intent)
    {
    }

    open fun handleResult(requestCode: Int?, code: Int?, data: Intent?)
    {
    }

    @CallSuper
    open fun handleRestoreInstanceState(savedInstanceState: Bundle)
    {
        StateSaver.restoreInstanceState(this, savedInstanceState)
    }

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now.
     */
    open fun handleReady()
    {
    }

    @CallSuper
    open fun handleSaveInstanceState(outState: Bundle)
    {
        StateSaver.saveInstanceState(this, outState)
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true)
    {
        _resultEvent.value = Result(code, intent, finish)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
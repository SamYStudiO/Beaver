@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "PropertyName")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.content.Intent
import android.os.Bundle
import io.reactivex.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel()
{
    @Inject
    protected lateinit var userManager: UserManager
    protected val _resultEvent: SingleLiveEvent<Result> by lazy { SingleLiveEvent<Result>() }
    val resultEvent: LiveData<Result> = _resultEvent
    val userStatusObservable: LiveData<Boolean> by lazy {
        LiveDataReactiveStreams.fromPublisher(
            userManager.statusObservable.toFlowable(BackpressureStrategy.LATEST))
    }
    protected val _navigationEvent: SingleLiveEvent<NavigationRequest> = SingleLiveEvent()
    val navigationEvent: LiveData<NavigationRequest> = _navigationEvent

    open fun handleCreate(savedInstanceState: Bundle?)
    {
    }

    open fun handleResult(requestCode: Int?, code: Int?, data: Intent?)
    {
    }

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now. Note: this may be called several times since it's called from
     * [android.support.v4.app.Fragment.onResume].
     */
    open fun handleReady()
    {
    }

    open fun handleSaveInstanceState(outState: Bundle)
    {
    }

    fun navigationRequest(navigationRequest: NavigationRequest)
    {
        _navigationEvent.value = navigationRequest
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
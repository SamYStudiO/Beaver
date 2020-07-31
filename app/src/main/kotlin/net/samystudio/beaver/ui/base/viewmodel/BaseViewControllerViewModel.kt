package net.samystudio.beaver.ui.base.viewmodel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.NavigationEvent
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent

abstract class BaseViewControllerViewModel : BaseViewModel() {
    private val savable = Bundle()
    private val _navigationCommand: NavigationEvent = NavigationEvent()
    val navigationCommand: LiveData<NavigationRequest> = _navigationCommand
    private val _resultEvent: SingleLiveEvent<Result> = SingleLiveEvent()
    val resultEvent: LiveData<Result> = _resultEvent

    open fun handleCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            savable.putAll(savedInstanceState.getBundle(getClassTag()))
    }

    open fun handleIntent(intent: Intent) {}

    open fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now.
     */
    open fun handleReady() {}

    @CallSuper
    open fun handleSaveInstanceState(outState: Bundle) {
        outState.putBundle(getClassTag(), savable)
    }

    open fun handleTrimMemory(level: Int) {}

    fun navigate(navigationRequest: NavigationRequest) {
        _navigationCommand.value = navigationRequest
    }

    /**
     * @see Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true) {
        _resultEvent.value = Result(code, intent, finish)
    }

    protected fun <T> state(
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.Nullable(savable, beforeSetCallback, afterSetCallback)

    protected fun <T> state(
        defaultValue: T,
        beforeSetCallback: ((value: T) -> T)? = null,
        afterSetCallback: ((value: T) -> Unit)? = null
    ) =
        InstanceStateProvider.NotNull(savable, defaultValue, beforeSetCallback, afterSetCallback)

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
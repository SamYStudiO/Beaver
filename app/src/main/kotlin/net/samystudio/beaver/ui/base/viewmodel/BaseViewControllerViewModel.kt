package net.samystudio.beaver.ui.base.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import io.reactivex.BackpressureStrategy
import net.samystudio.beaver.data.local.InstanceStateProvider
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ext.getClassTag
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.NavigationEvent
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel() {
    @Inject
    private val _navigationCommand: NavigationEvent = NavigationEvent()
    protected lateinit var userManager: UserManager
    val navigationCommand: LiveData<NavigationRequest> = _navigationCommand
    private val _resultEvent: SingleLiveEvent<Result> by lazy { SingleLiveEvent<Result>() }
    val resultEvent: LiveData<Result> = _resultEvent
    private val savable = Bundle()

    open fun handleCreate(savedInstanceState: Bundle?) {
        disposables.add(userManager.statusObservable.toFlowable(BackpressureStrategy.LATEST)
            .subscribe {
                if (it) handleUserConnected()
                else handleUserDisconnected()
            })
    }

    open fun handleIntent(intent: Intent) {}

    open fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    @CallSuper
    open fun handleRestoreInstanceState(savedInstanceState: Bundle) {
        savable.putAll(savedInstanceState.getBundle(getClassTag()))
    }

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

    open fun handleUserConnected() {}

    open fun handleUserDisconnected() {}

    fun navigate(navigationRequest: NavigationRequest) {
        _navigationCommand.value = navigationRequest
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true) {
        _resultEvent.value = Result(code, intent, finish)
    }

    protected fun <T> state(setterCallback: (value: T) -> Unit) =
        InstanceStateProvider.Nullable(savable, setterCallback)

    protected fun <T> state(defaultValue: T, setterCallback: (value: T) -> Unit) =
        InstanceStateProvider.NotNull(savable, defaultValue, setterCallback)

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
package net.samystudio.beaver.ui.base.viewmodel

//import com.evernote.android.state.StateSaver
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import io.reactivex.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import net.samystudio.beaver.ui.common.navigation.NavigationRequest
import net.samystudio.beaver.ui.common.viewmodel.NavigationEvent
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel() {
    @Inject
    protected lateinit var userManager: UserManager
    private val _navigationCommand: NavigationEvent = NavigationEvent()
    val navigationCommand: LiveData<NavigationRequest> = _navigationCommand
    private val _resultEvent: SingleLiveEvent<Result> by lazy { SingleLiveEvent<Result>() }
    val resultEvent: LiveData<Result> = _resultEvent

    open fun handleCreate() {
        disposables.add(userManager.statusObservable.toFlowable(BackpressureStrategy.LATEST)
            .subscribe {
                if (it) handleUserConnected()
                else handleUserDisconnected()
            })
    }

    open fun handleIntent(intent: Intent) {
    }

    open fun handleResult(requestCode: Int, code: Int, data: Intent?) {
    }

    @CallSuper
    open fun handleRestoreInstanceState(savedInstanceState: Bundle) {
        //StateSaver.restoreInstanceState(this, savedInstanceState)
    }

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now.
     */
    open fun handleReady() {
    }

    open fun handleUserConnected() {

    }

    open fun handleUserDisconnected() {

    }

    @CallSuper
    open fun handleSaveInstanceState(outState: Bundle) {
        //StateSaver.saveInstanceState(this, outState)
    }

    fun navigate(navigationRequest: NavigationRequest) {
        _navigationCommand.value = navigationRequest
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true) {
        _resultEvent.value = Result(code, intent, finish)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
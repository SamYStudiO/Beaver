@file:Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER", "PropertyName")

package net.samystudio.beaver.ui.base.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.os.Bundle
import io.reactivex.BackpressureStrategy
import net.samystudio.beaver.data.manager.UserManager
import javax.inject.Inject

abstract class BaseViewControllerViewModel : BaseViewModel()
{
    @Inject
    protected lateinit var userManager: UserManager
    protected val _resultObservable: MutableLiveData<Result> = MutableLiveData()
    val resultObservable: LiveData<Result> = _resultObservable
    protected val _userStatusObservable: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var userStatusObservable: LiveData<Boolean>
        private set

    open fun handleCreate()
    {
        userStatusObservable =
                LiveDataReactiveStreams.fromPublisher(userManager.statusObservable.toFlowable(
                    BackpressureStrategy.LATEST))
    }

    /**
     * This is called from view onResume, so may be called several time during view lifecycle. You
     * should make sure you've not already consume these parameters otherwise in some circumstance
     * it could lead to unexpected behaviours (for example a [android.widget.Toast] popping though
     * it already has been consumed).
     *
     * @param intent [Intent] from activity container, same as [android.app.Activity.getIntent].
     * @param savedInstanceState [Bundle] from activity or fragment container.
     * @param arguments [Bundle] from fragment container, same as
     * [android.support.v4.app.Fragment.getArguments], always null if container is an activity.
     * [requestCode] is always null with activity view models.
     * @param requestCode same as onActivityResult
     * @param resultCode same as onActivityResult
     * @param data same as onActivityResult
     */
    open fun handleState(intent: Intent,
                         savedInstanceState: Bundle?,
                         arguments: Bundle?,
                         requestCode: Int?,
                         resultCode: Int?,
                         data: Intent?)
    {
    }

    /**
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now. Called right after [handleState].
     */
    open fun handleReady()
    {
    }

    open fun handleSaveInstanceState(outState: Bundle)
    {
    }

    /**
     * @see android.app.Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true)
    {
        _resultObservable.value = Result(code, intent, finish)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
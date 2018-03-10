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
     * View model is up and ready, all kind of params (intent, savedInstanceState, arguments) should
     * be handled now.
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
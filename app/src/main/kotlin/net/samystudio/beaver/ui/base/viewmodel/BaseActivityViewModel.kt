package net.samystudio.beaver.ui.base.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent

abstract class BaseActivityViewModel : BaseViewControllerViewModel() {
    private val _resultEvent: SingleLiveEvent<Result> = SingleLiveEvent()
    val resultEvent: LiveData<Result> = _resultEvent

    /**
     * @see Activity.setResult
     */
    fun setResult(code: Int, intent: Intent?, finish: Boolean = true) {
        _resultEvent.value = Result(code, intent, finish)
    }

    data class Result(var code: Int, var intent: Intent?, var finish: Boolean)
}
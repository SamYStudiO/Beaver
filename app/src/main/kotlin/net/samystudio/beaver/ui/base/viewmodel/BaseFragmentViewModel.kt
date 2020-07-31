package net.samystudio.beaver.ui.base.viewmodel

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import net.samystudio.beaver.ui.common.viewmodel.SingleLiveEvent

abstract class BaseFragmentViewModel : BaseViewControllerViewModel() {
    private val _resultEvent: SingleLiveEvent<Result> = SingleLiveEvent()
    val resultEvent: LiveData<Result> = _resultEvent

    open fun handleArguments(argument: Bundle) {}

    fun setResult(key: String, value: Any, finish: Boolean = true) {
        _resultEvent.value = Result(bundleOf(key to value), finish)
    }

    fun setResult(bundle: Bundle, finish: Boolean = true) {
        _resultEvent.value = Result(bundle, finish)
    }

    data class Result(var bundle: Bundle, var finish: Boolean)
}
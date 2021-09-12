package net.samystudio.beaver.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import net.samystudio.beaver.data.ResultAsyncState

class TriggerResultStateFlow<IN, OUT>(
    private val isTriggerWhenStarted: Boolean = false,
    private val initialData: IN? = null,
    private val callee: suspend (IN) -> OUT
) {
    private val flow = MutableStateFlow<ResultAsyncState<OUT>>(ResultAsyncState.Idle())
    private var lastData: IN? = null

    init {
        flow.onStart {
            if (isTriggerWhenStarted) {
                (lastData ?: initialData)?.let { trigger(it) }
            }
        }
    }

    suspend fun trigger(data: IN) {
        if (flow.value is ResultAsyncState.Idle) {
            flow.emit(ResultAsyncState.Started())
            try {
                flow.emit(ResultAsyncState.Completed(callee.invoke(data)))
            } catch (e: Throwable) {
                flow.emit(ResultAsyncState.Failed(e))
            } finally {
                flow.emit(ResultAsyncState.Idle())
            }
        }
    }

    suspend fun collect(action: suspend (value: ResultAsyncState<OUT>) -> Unit) =
        flow.collect(action)
}

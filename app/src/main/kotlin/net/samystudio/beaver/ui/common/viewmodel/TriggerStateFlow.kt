package net.samystudio.beaver.ui.common.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState

class TriggerStateFlow(
    private val isTriggerWhenStarted: Boolean = false,
    private val callee: suspend () -> Unit
) {
    private val flow = MutableStateFlow<AsyncState>(AsyncState.Idle)
    val value
        get() = flow.value

    init {
        flow.onStart {
            if (isTriggerWhenStarted) {
                trigger()
            }
        }
    }

    suspend fun trigger() {
        if (flow.value is AsyncState.Idle) {
            flow.emit(AsyncState.Started)
            try {
                callee.invoke()
                flow.emit(AsyncState.Completed)
            } catch (e: Throwable) {
                flow.emit(AsyncState.Failed(e))
            } finally {
                flow.emit(AsyncState.Idle)
            }
        }
    }

    suspend fun collect(action: suspend (value: AsyncState) -> Unit) =
        flow.collect(action)
}

class TriggerInStateFlow<IN>(
    private val isTriggerWhenStarted: Boolean = false,
    private val initialData: IN? = null,
    private val callee: suspend (IN) -> Unit
) {
    private val flow = MutableStateFlow<AsyncState>(AsyncState.Idle)
    private var lastData: IN? = null

    init {
        flow.onStart {
            if (isTriggerWhenStarted) {
                (lastData ?: initialData)?.let { trigger(it) }
            }
        }
    }

    suspend fun trigger(data: IN) {
        if (flow.value is AsyncState.Idle) {
            lastData = data
            flow.emit(AsyncState.Started)
            try {
                callee.invoke(data)
                flow.emit(AsyncState.Completed)
            } catch (e: Throwable) {
                flow.emit(AsyncState.Failed(e))
            } finally {
                flow.emit(AsyncState.Idle)
            }
        }
    }

    suspend fun collect(action: suspend (value: AsyncState) -> Unit) =
        flow.collect(action)
}

class TriggerOutStateFlow<OUT>(
    private val isTriggerWhenStarted: Boolean = false,
    private val callee: suspend () -> OUT
) {
    private val flow = MutableStateFlow<ResultAsyncState<OUT>>(ResultAsyncState.Idle())

    init {
        flow.onStart {
            if (isTriggerWhenStarted) {
                trigger()
            }
        }
    }

    suspend fun trigger() {
        if (flow.value is ResultAsyncState.Idle) {
            flow.emit(ResultAsyncState.Started())
            try {
                flow.emit(ResultAsyncState.Completed(callee.invoke()))
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

class TriggerInOutStateFlow<IN, OUT>(
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

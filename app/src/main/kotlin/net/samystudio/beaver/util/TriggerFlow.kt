package net.samystudio.beaver.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import net.samystudio.beaver.data.AsyncState
import net.samystudio.beaver.data.ResultAsyncState

class TriggerFlow(
    private val isTriggerWhenSubscribed: Boolean = false,
    private val callee: () -> Flow<Unit>
) : Flow<Unit> {
    private val triggerInFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    private val triggerOutFlow: Flow<Unit> = triggerInFlow
        .let {
            if (isTriggerWhenSubscribed)
                it.onSubscription {
                    emit(Unit)
                }
            else
                it
        }
        .flatMapLatest {
            callee()
        }

    override suspend fun collect(collector: FlowCollector<Unit>) {
        triggerOutFlow.collect(collector)
    }

    fun trigger() {
        triggerInFlow.tryEmit(Unit)
    }
}

class TriggerInFlow<IN>(
    private val isTriggerWhenSubscribed: Boolean = false,
    private val initialTriggerData: (() -> IN)? = null,
    private val useLastTriggeredDataAsInitialIfAvailable: Boolean = true,
    private val callee: (IN) -> Flow<Unit>
) : Flow<Unit> {
    private val triggerInFlow = MutableSharedFlow<IN>(0, 1, BufferOverflow.DROP_OLDEST)
    private val triggerOutFlow: Flow<Unit> = triggerInFlow
        .let { flow ->
            if (isTriggerWhenSubscribed)
                flow.onSubscription {
                    val triggerData: IN? =
                        lastTriggeredData
                            ?.takeIf { useLastTriggeredDataAsInitialIfAvailable }
                            ?: initialTriggerData?.invoke()
                    triggerData?.let { emit(it) }
                }
            else
                flow
        }
        .flatMapLatest {
            lastTriggeredData = it
            callee(it)
        }
    private var lastTriggeredData: IN? = null

    init {
        if (isTriggerWhenSubscribed)
            requireNotNull(initialTriggerData) {
                "When isTriggerWhenSubscribed is true a initialTriggerData must be set as well."
            }
    }

    override suspend fun collect(collector: FlowCollector<Unit>) {
        triggerOutFlow.collect(collector)
    }

    fun trigger(data: IN) {
        triggerInFlow.tryEmit(data)
    }
}

open class TriggerOutFlow<OUT>(
    private val isTriggerWhenSubscribed: Boolean = false,
    private val callee: () -> Flow<OUT>
) : Flow<OUT> {
    private val triggerInFlow = MutableSharedFlow<Unit>(0, 1, BufferOverflow.DROP_OLDEST)
    private val triggerOutFlow: Flow<OUT> = triggerInFlow
        .let {
            if (isTriggerWhenSubscribed)
                it.onSubscription {
                    emit(Unit)
                }
            else
                it
        }
        .flatMapLatest {
            callee()
        }
        .onEach {
            currentValue = it
        }
    var currentValue: OUT? = null
        private set

    override suspend fun collect(collector: FlowCollector<OUT>) {
        triggerOutFlow.collect(collector)
    }

    open fun trigger() {
        triggerInFlow.tryEmit(Unit)
    }
}

open class TriggerInOutFlow<IN, OUT>(
    private val isTriggerWhenSubscribed: Boolean = false,
    private val initialTriggerData: (() -> IN)? = null,
    private val useLastTriggeredDataAsInitialIfAvailable: Boolean = true,
    private val callee: suspend (IN) -> Flow<OUT>
) : Flow<OUT> {
    private val triggerInFlow = MutableSharedFlow<IN>(0, 1, BufferOverflow.DROP_OLDEST)
    private val triggerOutFlow: Flow<OUT> = triggerInFlow
        .let { flow ->
            if (isTriggerWhenSubscribed)
                flow.onSubscription {
                    val triggerData: IN? =
                        lastTriggeredData
                            ?.takeIf { useLastTriggeredDataAsInitialIfAvailable }
                            ?: initialTriggerData?.invoke()
                    triggerData?.let { emit(it) }
                }
            else
                flow
        }
        .flatMapLatest {
            lastTriggeredData = it
            callee(it)
        }
        .onEach {
            currentValue = it
        }
    protected var lastTriggeredData: IN? = null
    var currentValue: OUT? = null
        private set

    init {
        if (isTriggerWhenSubscribed)
            requireNotNull(initialTriggerData) {
                "When isTriggerWhenSubscribed is true a initialTriggerData must be set as well."
            }
    }

    override suspend fun collect(collector: FlowCollector<OUT>) {
        triggerOutFlow.collect(collector)
    }

    open fun trigger(data: IN) {
        triggerInFlow.tryEmit(data)
    }
}

class TriggerAsyncStateFlow(
    isTriggerWhenSubscribed: Boolean = false,
    callee: () -> Flow<AsyncState>
) : TriggerOutFlow<AsyncState>(isTriggerWhenSubscribed, callee) {
    override fun trigger() {
        //  Don't need to trigger if we're already in a process to get result.
        if (currentValue !is AsyncState.Started)
            super.trigger()
    }
}

class TriggerInAsyncStateFlow<IN>(
    isTriggerWhenSubscribed: Boolean = false,
    initialTriggerData: (() -> IN)? = null,
    useLastTriggeredDataAsInitialIfAvailable: Boolean = true,
    callee: (IN) -> Flow<AsyncState>
) : TriggerInOutFlow<IN, AsyncState>(
    isTriggerWhenSubscribed,
    initialTriggerData,
    useLastTriggeredDataAsInitialIfAvailable,
    callee
) {
    override fun trigger(data: IN) {
        //  Don't need to trigger if we're already in a process to get result with specified data.
        if (data != lastTriggeredData || currentValue !is AsyncState.Started)
            super.trigger(data)
    }
}

class TriggerResultAsyncStateFlow<D>(
    isTriggerWhenSubscribed: Boolean = false,
    callee: () -> Flow<ResultAsyncState<D>>
) : TriggerOutFlow<ResultAsyncState<D>>(isTriggerWhenSubscribed, callee) {
    override fun trigger() {
        //  Don't need to trigger if we're already in a process to get result.
        if (currentValue !is ResultAsyncState.Started<*>)
            super.trigger()
    }
}

class TriggerInResultAsyncStateFlow<IN, D>(
    isTriggerWhenSubscribed: Boolean = false,
    initialTriggerData: (() -> IN)? = null,
    useLastTriggeredDataAsInitialIfAvailable: Boolean = true,
    callee: (IN) -> Flow<ResultAsyncState<D>>
) : TriggerInOutFlow<IN, ResultAsyncState<D>>(
    isTriggerWhenSubscribed,
    initialTriggerData,
    useLastTriggeredDataAsInitialIfAvailable,
    callee
) {
    override fun trigger(data: IN) {
        //  Don't need to trigger if we're already in a process to get result with specified data.
        if (data != lastTriggeredData || currentValue !is ResultAsyncState.Started<*>)
            super.trigger(data)
    }
}



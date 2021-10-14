@file:Suppress("NOTHING_TO_INLINE", "unused")

package net.samystudio.beaver.ui.common.viewmodel

import androidx.lifecycle.LiveData

/**
 * A [SingleLiveEvent] with no data associated.
 */
class CommandLiveEvent : SingleLiveEvent<Unit>()

inline fun LiveData<*>.toCommandLiveEvent() =
    CommandLiveEvent().apply {
        addSource(this) {
            call()
        }
    }

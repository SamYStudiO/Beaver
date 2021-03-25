@file:Suppress("unused")

package net.samystudio.beaver.util

import net.samystudio.beaver.ContextProvider
import net.samystudio.beaver.R

// Transition
val TRANSITION_DURATION =
    ContextProvider.applicationContext.resources.getInteger(R.integer.transition_duration).toLong()
val TRANSITION_DURATION_SHORT =
    ContextProvider.applicationContext.resources.getInteger(R.integer.transition_duration_short)
        .toLong()

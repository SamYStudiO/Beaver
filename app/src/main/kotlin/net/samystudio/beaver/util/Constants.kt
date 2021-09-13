@file:Suppress("unused")

package net.samystudio.beaver.util

import net.samystudio.beaver.ContextProvider.Companion.applicationContext
import net.samystudio.beaver.R

// Transition
val TRANSITION_DURATION =
    applicationContext.resources.getInteger(R.integer.transition_duration).toLong()
val TRANSITION_DURATION_SHORT =
    applicationContext.resources.getInteger(R.integer.transition_duration_short).toLong()

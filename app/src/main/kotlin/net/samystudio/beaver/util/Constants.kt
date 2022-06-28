@file:Suppress("unused")

package net.samystudio.beaver.util

import net.samystudio.beaver.ContextProvider.Companion.applicationContext
import net.samystudio.beaver.R

const val PASSWORD_MIN_LENGTH = 6
const val PASSWORD_MAX_LENGTH = 100
const val NETWORK_CACHE_SIZE = 20L * 1024 * 1024 // 20 mo
// Transition
val TRANSITION_DURATION =
    applicationContext.resources.getInteger(R.integer.transition_duration).toLong()
val TRANSITION_DURATION_SHORT =
    applicationContext.resources.getInteger(R.integer.transition_duration_short).toLong()

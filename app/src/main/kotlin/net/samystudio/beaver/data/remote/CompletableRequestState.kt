@file:Suppress("unused")

package net.samystudio.beaver.data.remote

/**
 * Async request states, response contains no data.
 */
sealed class CompletableRequestState {
    class Start : CompletableRequestState()
    class Complete : CompletableRequestState()
    class Error(val error: Throwable) : CompletableRequestState()
}
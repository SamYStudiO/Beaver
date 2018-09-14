package net.samystudio.beaver.data.remote

/**
 * Async request states, response contains no data.
 */
sealed class CompletableRequestState {
    object Start : CompletableRequestState()
    object Complete : CompletableRequestState()
    class Error(val error: Throwable) : CompletableRequestState()
}
package net.samystudio.beaver.data

/**
 * Async request states, response contains no data.
 */
sealed class AsyncState {
    object Started : AsyncState()
    object Completed : AsyncState()
    class Failed(val error: Throwable) : AsyncState()
}
@file:Suppress("unused")

package net.samystudio.beaver.data.remote

/**
 * Async request states, response contains no data.
 */
sealed class CommandRequestState
{
    class Start : CommandRequestState()
    class Complete : CommandRequestState()
    class Error(val error: Throwable) : CommandRequestState()
}
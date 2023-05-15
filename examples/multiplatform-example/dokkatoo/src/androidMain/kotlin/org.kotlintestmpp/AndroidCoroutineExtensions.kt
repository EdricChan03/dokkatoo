package org.kotlintestmpp.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred

/**
 * Android actual implementation for `asyncWithDelay`
 */
actual fun <T> CoroutineScope.asyncWithDelay(delay: Long, block: suspend () -> T): Deferred<T> {
  TODO("Not yet implemented")
}

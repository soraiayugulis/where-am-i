package com.whereami.data.network

import com.whereami.core.network.RetryPolicy
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.delay

class RetryPolicyImpl @Inject constructor() : RetryPolicy {
    private val maxRetries = 3
    private val initialDelayMs = 500L

    override suspend fun <T> execute(block: suspend () -> T): T {
        var lastException: Exception? = null
        repeat(maxRetries) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                if (e.isRetryable()) {
                    lastException = e
                    delay(initialDelayMs * (attempt + 1))
                } else {
                    throw e
                }
            }
        }
        throw lastException ?: IllegalStateException("Retry exhausted")
    }

    private fun Throwable.isRetryable(): Boolean = this is IOException
}

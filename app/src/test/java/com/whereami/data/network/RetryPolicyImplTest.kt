package com.whereami.data.network

import com.whereami.core.network.RetryPolicy
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RetryPolicyImplTest {
    @Test
    fun `returns result on first attempt`() = runTest {
        val policy: RetryPolicy = RetryPolicyImpl()
        val result = policy.execute { "ok" }
        assertEquals("ok", result)
    }

    @Test
    fun `retries on IOException and succeeds`() = runTest {
        val policy: RetryPolicy = RetryPolicyImpl()
        var attempts = 0
        val result = policy.execute {
            attempts++
            if (attempts < 3) throw IOException("fail")
            "ok"
        }
        assertEquals("ok", result)
        assertEquals(3, attempts)
    }

    @Test
    fun `throws after max retries`() = runTest {
        val policy: RetryPolicy = RetryPolicyImpl()
        var caught: Boolean
        try {
            policy.execute { throw IOException("always fails") }
        } catch (e: IOException) {
            caught = true
            assertEquals("always fails", e.message)
        }
        assertTrue(caught)
    }

    @Test
    fun `does not retry on non retryable exception`() = runTest {
        val policy: RetryPolicy = RetryPolicyImpl()
        var attempts = 0
        var caught: Boolean
        try {
            policy.execute {
                attempts++
                throw IllegalArgumentException("no retry")
            }
        } catch (e: IllegalArgumentException) {
            caught = true
            assertEquals("no retry", e.message)
        }
        assertTrue(caught)
        assertEquals(1, attempts)
    }
}

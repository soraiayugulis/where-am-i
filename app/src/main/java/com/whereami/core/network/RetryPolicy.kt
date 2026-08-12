package com.whereami.core.network

interface RetryPolicy {
    suspend fun <T> execute(block: suspend () -> T): T
}

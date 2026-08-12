package com.whereami.core.time

class FakeClock(private var current: Long = 0L) : Clock {
    fun advance(millis: Long) {
        current += millis
    }

    override fun now(): Long = current
}

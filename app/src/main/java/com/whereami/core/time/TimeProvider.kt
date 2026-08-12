package com.whereami.core.time

interface TimeProvider {
    fun nowMs(): Long
}

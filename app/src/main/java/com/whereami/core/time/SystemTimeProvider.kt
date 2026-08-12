package com.whereami.core.time

import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {
    override fun nowMs(): Long = System.currentTimeMillis()
}

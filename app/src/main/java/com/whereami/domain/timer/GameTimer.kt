package com.whereami.domain.timer

import com.whereami.core.time.Clock
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val GAME_DURATION_SECONDS = 150
private const val WARNING_THRESHOLD_SECONDS = 25

class GameTimer @Inject constructor(
    private val clock: Clock
) {
    private val _remainingTime = MutableStateFlow(GAME_DURATION_SECONDS)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _isWarning = MutableStateFlow(false)
    val isWarning: StateFlow<Boolean> = _isWarning

    private var startTime: Long = 0L
    private var job: Job? = null
    private var lastScope: CoroutineScope? = null
    private var isPaused = false
    private var remainingAtPause: Int = GAME_DURATION_SECONDS

    fun start(scope: CoroutineScope) {
        lastScope = scope
        startTime = clock.now()
        _remainingTime.value = GAME_DURATION_SECONDS
        _isWarning.value = false
        isPaused = false
        startJob(scope)
    }

    fun pause() {
        job?.cancel()
        isPaused = true
        remainingAtPause = _remainingTime.value
    }

    fun resume() {
        if (!isPaused) return
        val scope = lastScope ?: return
        val elapsedMs = (GAME_DURATION_SECONDS - remainingAtPause) * 1000L
        startTime = clock.now() - elapsedMs
        _remainingTime.value = remainingAtPause
        _isWarning.value = remainingAtPause <= WARNING_THRESHOLD_SECONDS
        isPaused = false
        startJob(scope)
    }

    fun stop() {
        job?.cancel()
        isPaused = false
    }

    private fun startJob(scope: CoroutineScope) {
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                delay(1000)
                val elapsed = (clock.now() - startTime) / 1000
                val remaining = (GAME_DURATION_SECONDS - elapsed).toInt().coerceAtLeast(0)
                _remainingTime.value = remaining
                _isWarning.value = remaining <= WARNING_THRESHOLD_SECONDS
                if (remaining <= 0) break
            }
        }
    }
}

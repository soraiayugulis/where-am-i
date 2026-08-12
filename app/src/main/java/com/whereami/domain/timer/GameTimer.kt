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

class GameTimer @Inject constructor(
    private val clock: Clock
) {
    private val _remainingTime = MutableStateFlow(GAME_DURATION_SECONDS)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _isWarning = MutableStateFlow(false)
    val isWarning: StateFlow<Boolean> = _isWarning

    private var startTime: Long = 0L
    private var job: Job? = null

    fun start(scope: CoroutineScope) {
        startTime = clock.now()
        _remainingTime.value = GAME_DURATION_SECONDS
        _isWarning.value = false
        job?.cancel()
        job = scope.launch {
            while (isActive) {
                delay(1000)
                val elapsed = (clock.now() - startTime) / 1000
                val remaining = (GAME_DURATION_SECONDS - elapsed).toInt().coerceAtLeast(0)
                _remainingTime.value = remaining
                _isWarning.value = remaining <= 10
                if (remaining <= 0) break
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}

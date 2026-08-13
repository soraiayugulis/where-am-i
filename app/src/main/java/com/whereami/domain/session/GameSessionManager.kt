package com.whereami.domain.session

import com.whereami.core.time.Clock
import com.whereami.domain.model.Location
import com.whereami.domain.model.MatchResult
import com.whereami.domain.model.Status
import com.whereami.domain.repository.CountryResolver
import com.whereami.domain.timer.GameTimer
import com.whereami.domain.usecase.CalculateScoreUseCase
import com.whereami.domain.usecase.GetRandomLocationUseCase
import com.whereami.domain.usecase.SaveMatchUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val GAME_DURATION_MS = 150_000L

@Singleton
class GameSessionManager @Inject constructor(
    private val clock: Clock,
    private val gameTimer: GameTimer,
    private val getRandomLocation: GetRandomLocationUseCase,
    private val countryResolver: CountryResolver,
    private val calculateScore: CalculateScoreUseCase,
    private val saveMatch: SaveMatchUseCase
) {
    private val _state = MutableStateFlow<GameSessionState>(GameSessionState.Idle)
    val state: StateFlow<GameSessionState> = _state

    private var startTime: Long = 0L
    private var target: Location? = null
    private var job: Job? = null

    fun start(locationSeed: Int, scope: CoroutineScope) {
        val target = getRandomLocation(locationSeed).getOrThrow()
        this.target = target
        startTime = clock.now()
        _state.value = GameSessionState.Playing(target, 150, false)
        gameTimer.start(scope)
        job?.cancel()
        job = scope.launch {
            gameTimer.remainingTime.collect { remaining ->
                if (_state.value is GameSessionState.Playing) {
                    _state.value = GameSessionState.Playing(
                        target,
                        remaining,
                        gameTimer.isWarning.value
                    )
                    if (remaining == 0) {
                        expire()
                    }
                }
            }
        }
    }

    suspend fun submitGuess(guess: Location) {
        val target = this.target ?: return
        if (_state.value is GameSessionState.Finished) return
        stop()
        val elapsed = clock.now() - startTime
        val targetCountry = countryResolver.resolve(target)
        val guessCountry = countryResolver.resolve(guess)
        val score = calculateScore(target, guess, targetCountry, guessCountry)
        val match = MatchResult(
            datePlayed = clock.now(),
            target = target,
            guess = guess,
            timeTakenMs = elapsed,
            score = score,
            status = Status.COMPLETED
        )
        saveMatch(match)
        _state.value = GameSessionState.Finished(match)
    }

    suspend fun expire() {
        val target = this.target ?: return
        if (_state.value is GameSessionState.Finished) return
        val match = MatchResult(
            datePlayed = clock.now(),
            target = target,
            guess = null,
            timeTakenMs = GAME_DURATION_MS,
            score = 0,
            status = Status.INCOMPLETE
        )
        _state.value = GameSessionState.Finished(match)
        saveMatch(match)
        stop()
    }

    fun stop() {
        gameTimer.stop()
        job?.cancel()
    }

    fun pauseTimerIfPlaying() {
        if (state.value is GameSessionState.Playing) gameTimer.pause()
    }

    fun resumeTimerIfPlaying() {
        if (state.value is GameSessionState.Playing) gameTimer.resume()
    }
}

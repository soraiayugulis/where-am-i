package com.whereami.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.usecase.GetWeeklyScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeeklyScore: GetWeeklyScoreUseCase
) : ViewModel() {
    private val _weeklyScore = MutableStateFlow(0)
    val weeklyScore: StateFlow<Int> = _weeklyScore

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _weeklyScore.value = getWeeklyScore()
    }
}

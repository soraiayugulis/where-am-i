package com.whereami.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.model.MatchResult
import com.whereami.domain.usecase.GetMatchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistory: GetMatchHistoryUseCase
) : ViewModel() {
    private val _matches = MutableStateFlow<List<MatchResult>>(emptyList())
    val matches: StateFlow<List<MatchResult>> = _matches

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        _matches.value = getHistory()
    }
}

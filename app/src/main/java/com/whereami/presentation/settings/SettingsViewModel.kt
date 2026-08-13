package com.whereami.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whereami.domain.model.Language
import com.whereami.domain.usecase.ClearAllMatchesUseCase
import com.whereami.domain.usecase.GetLanguageUseCase
import com.whereami.domain.usecase.SetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val clearAllMatchesUseCase: ClearAllMatchesUseCase
) : ViewModel() {

    private val _language = MutableStateFlow(Language.EN)
    val language: StateFlow<Language> = _language

    private val _cleared = MutableStateFlow(false)
    val cleared: StateFlow<Boolean> = _cleared

    init {
        loadLanguage()
    }

    private fun loadLanguage() = viewModelScope.launch {
        _language.value = getLanguageUseCase()
    }

    fun setLanguage(language: Language) = viewModelScope.launch {
        setLanguageUseCase(language)
        _language.value = language
    }

    fun clearHistory() = viewModelScope.launch {
        clearAllMatchesUseCase()
        _cleared.value = true
    }
}

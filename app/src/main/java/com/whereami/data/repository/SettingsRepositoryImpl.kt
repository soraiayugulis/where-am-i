package com.whereami.data.repository

import android.content.Context
import com.whereami.core.locale.LocaleManager
import com.whereami.domain.model.Language
import com.whereami.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    override suspend fun getLanguage(): Language = LocaleManager.getLanguage(context)

    override suspend fun setLanguage(language: Language) {
        LocaleManager.setLanguage(context, language)
    }
}

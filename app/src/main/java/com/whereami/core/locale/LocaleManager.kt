package com.whereami.core.locale

import android.content.Context
import android.content.res.Configuration
import com.whereami.domain.model.Language
import java.util.Locale

object LocaleManager {
    private const val PREFS_NAME = "whereami_settings"
    private const val KEY_LANGUAGE = "language"

    fun setLanguage(context: Context, language: Language) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, language.tag)
            .apply()
    }

    fun getLanguage(context: Context): Language {
        val tag = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, Language.EN.tag)
        return Language.entries.find { it.tag == tag } ?: Language.EN
    }

    fun attachBaseContext(base: Context): Context {
        val language = getLanguage(base)
        val locale = Locale.forLanguageTag(language.tag)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}

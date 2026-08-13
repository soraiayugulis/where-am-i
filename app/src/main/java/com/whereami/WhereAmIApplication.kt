package com.whereami

import android.app.Application
import android.content.Context
import com.whereami.core.locale.LocaleManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WhereAmIApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        val newBase = base?.let { LocaleManager.attachBaseContext(it) } ?: base
        super.attachBaseContext(newBase)
    }
}

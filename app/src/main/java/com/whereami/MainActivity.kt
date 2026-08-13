package com.whereami

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.whereami.core.locale.LocaleManager
import com.whereami.presentation.navigation.AppNavigation
import com.whereami.presentation.theme.WhereAmITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.let { LocaleManager.attachBaseContext(it) } ?: newBase
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhereAmITheme {
                AppNavigation()
            }
        }
    }
}

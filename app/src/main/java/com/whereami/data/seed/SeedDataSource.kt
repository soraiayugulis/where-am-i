package com.whereami.data.seed

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

interface SeedDataSource {
    @Throws(IOException::class)
    fun load(): String
}

class AssetSeedDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : SeedDataSource {
    override fun load(): String = context.assets.open("seeds.json").bufferedReader().use { it.readText() }
}

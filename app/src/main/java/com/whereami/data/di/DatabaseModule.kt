package com.whereami.data.di

import android.content.Context
import androidx.room.Room
import com.whereami.data.local.MatchDao
import com.whereami.data.local.MatchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMatchDatabase(@ApplicationContext context: Context): MatchDatabase {
        return Room.databaseBuilder(context, MatchDatabase::class.java, "matches.db").build()
    }

    @Provides
    fun provideMatchDao(database: MatchDatabase): MatchDao = database.matchDao()
}

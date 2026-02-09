package dev.sethdegay.routines.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.routines.core.database.SequenceDatabase
import dev.sethdegay.routines.core.database.dao.CalendarEventDao
import dev.sethdegay.routines.core.database.dao.SequenceDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSequenceDatabase(
        @ApplicationContext context: Context,
    ): SequenceDatabase = Room.databaseBuilder(
        context = context,
        klass = SequenceDatabase::class.java,
        name = "sequence.db",
    ).build()

    @Provides
    fun provideCalendarEventDao(database: SequenceDatabase): CalendarEventDao =
        database.calendarEventDao()

    @Provides
    fun provideSequenceDao(database: SequenceDatabase): SequenceDao =
        database.sequenceDao()
}
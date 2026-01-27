package dev.sethdegay.routines.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.routines.core.database.RoutinesDatabase
import dev.sethdegay.routines.core.database.dao.CalendarEventDao
import dev.sethdegay.routines.core.database.dao.RoutineDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoutinesDatabase(
        @ApplicationContext context: Context,
    ): RoutinesDatabase = Room.databaseBuilder(
        context = context,
        klass = RoutinesDatabase::class.java,
        name = "routines.db",
    ).build()

    @Provides
    fun provideCalendarEventDao(database: RoutinesDatabase): CalendarEventDao =
        database.calendarEventDao()

    @Provides
    fun provideRoutineDao(database: RoutinesDatabase): RoutineDao =
        database.routineDao()
}
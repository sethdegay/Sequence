package dev.sethdegay.routines.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.routines.core.data.repository.CalendarEventRepository
import dev.sethdegay.routines.core.data.repository.LocalCalendarEventRepository
import dev.sethdegay.routines.core.data.repository.LocalRoutineRepository
import dev.sethdegay.routines.core.data.repository.LocalUserPreferencesRepository
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.data.repository.UserPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindCalendarEventRepository(
        calendarEventRepository: LocalCalendarEventRepository
    ): CalendarEventRepository

    @Binds
    abstract fun bindRoutineRepository(
        routineRepository: LocalRoutineRepository
    ): RoutineRepository

    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: LocalUserPreferencesRepository
    ): UserPreferencesRepository
}
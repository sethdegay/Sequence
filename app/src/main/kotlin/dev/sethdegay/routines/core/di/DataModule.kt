package dev.sethdegay.routines.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.routines.core.data.repository.CalendarEventRepository
import dev.sethdegay.routines.core.data.repository.LocalCalendarEventRepository
import dev.sethdegay.routines.core.data.repository.LocalSequenceRepository
import dev.sethdegay.routines.core.data.repository.LocalUserPreferencesRepository
import dev.sethdegay.routines.core.data.repository.SequenceRepository
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
        routineRepository: LocalSequenceRepository
    ): SequenceRepository

    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: LocalUserPreferencesRepository
    ): UserPreferencesRepository
}
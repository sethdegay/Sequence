package dev.sethdegay.sequence.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.sequence.core.data.repository.CalendarEventRepository
import dev.sethdegay.sequence.core.data.repository.LocalCalendarEventRepository
import dev.sethdegay.sequence.core.data.repository.LocalSegmentRepository
import dev.sethdegay.sequence.core.data.repository.LocalSequenceRepository
import dev.sethdegay.sequence.core.data.repository.LocalUserPreferencesRepository
import dev.sethdegay.sequence.core.data.repository.LocalWorkspaceRepository
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.data.repository.WorkspaceRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindCalendarEventRepository(
        calendarEventRepository: LocalCalendarEventRepository
    ): CalendarEventRepository

    @Binds
    abstract fun bindSegmentRepository(
        segmentRepository: LocalSegmentRepository
    ): SegmentRepository

    @Binds
    abstract fun bindSequenceRepository(
        sequenceRepository: LocalSequenceRepository
    ): SequenceRepository

    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: LocalUserPreferencesRepository
    ): UserPreferencesRepository

    @Binds
    abstract fun bindWorkspaceRepository(
        workspaceRepository: LocalWorkspaceRepository
    ): WorkspaceRepository
}
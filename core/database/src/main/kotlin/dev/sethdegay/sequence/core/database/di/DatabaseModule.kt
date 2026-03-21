package dev.sethdegay.sequence.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.sethdegay.sequence.core.common.di.ApplicationScope
import dev.sethdegay.sequence.core.database.SequenceDatabase
import dev.sethdegay.sequence.core.database.WorkspacePreloadCallback
import dev.sethdegay.sequence.core.database.dao.CalendarEventDao
import dev.sethdegay.sequence.core.database.dao.SegmentDao
import dev.sethdegay.sequence.core.database.dao.SequenceDao
import dev.sethdegay.sequence.core.database.dao.WorkspaceDao
import kotlinx.coroutines.CoroutineScope
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSequenceDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        workspaceDaoProvider: Provider<WorkspaceDao>,
    ): SequenceDatabase = Room.databaseBuilder(
        context = context,
        klass = SequenceDatabase::class.java,
        name = "sequence.db",
    )
        .addCallback(WorkspacePreloadCallback(scope) { workspaceDaoProvider.get() })
        .build()

    @Provides
    fun provideCalendarEventDao(database: SequenceDatabase): CalendarEventDao =
        database.calendarEventDao()

    @Provides
    fun provideSegmentDao(database: SequenceDatabase): SegmentDao =
        database.segmentDao()

    @Provides
    fun provideSequenceDao(database: SequenceDatabase): SequenceDao =
        database.sequenceDao()

    @Provides
    fun provideWorkspaceDao(database: SequenceDatabase): WorkspaceDao =
        database.workspaceDao()
}
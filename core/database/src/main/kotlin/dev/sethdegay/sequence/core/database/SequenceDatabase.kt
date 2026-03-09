package dev.sethdegay.sequence.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sethdegay.sequence.core.database.dao.CalendarEventDao
import dev.sethdegay.sequence.core.database.dao.SequenceDao
import dev.sethdegay.sequence.core.database.dao.WorkspaceDao
import dev.sethdegay.sequence.core.database.model.CalendarEventEntity
import dev.sethdegay.sequence.core.database.model.SequenceEntity
import dev.sethdegay.sequence.core.database.model.StepEntity
import dev.sethdegay.sequence.core.database.model.WorkspaceEntity
import dev.sethdegay.sequence.core.database.util.DurationLongConverter
import dev.sethdegay.sequence.core.database.util.InstantLongConverter
import dev.sethdegay.sequence.core.database.util.LocalDateStringConverter
import dev.sethdegay.sequence.core.database.util.UuidByteConverter

@Database(
    entities = [
        CalendarEventEntity::class,
        SequenceEntity::class,
        StepEntity::class,
        WorkspaceEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        DurationLongConverter::class,
        InstantLongConverter::class,
        LocalDateStringConverter::class,
        UuidByteConverter::class,
    ],
)
abstract class SequenceDatabase : RoomDatabase() {
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun sequenceDao(): SequenceDao
    abstract fun workspaceDao(): WorkspaceDao
}
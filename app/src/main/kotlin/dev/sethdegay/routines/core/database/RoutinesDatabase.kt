package dev.sethdegay.routines.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.sethdegay.routines.core.database.dao.RoutineDao
import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.database.model.TaskEntity
import dev.sethdegay.routines.core.database.util.DurationConverter
import dev.sethdegay.routines.core.database.util.InstantConverter
import dev.sethdegay.routines.core.database.util.RoutineTypeConverter

@Database(
    entities = [
        TaskEntity::class,
        RoutineEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        DurationConverter::class,
        InstantConverter::class,
        RoutineTypeConverter::class,
    ],
)
abstract class RoutinesDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
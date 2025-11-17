package dev.sethdegay.routines.core.database.util

import androidx.room.TypeConverter
import dev.sethdegay.routines.core.database.model.RoutineTypeData
import dev.sethdegay.routines.core.database.model.RoutineTypeData.WorkoutData
import kotlinx.serialization.protobuf.ProtoBuf

class RoutineProtobufConverter {

    private val protobuf = ProtoBuf

    @TypeConverter
    fun routineTypeDataToWorkoutData(data: RoutineTypeData?): WorkoutData? = data as? WorkoutData

    @TypeConverter
    fun workoutDataToProtoBytes(data: WorkoutData?): ByteArray? {
        if (data == null) return null
        return protobuf.encodeToByteArray(WorkoutData.serializer(), data)
    }

    @TypeConverter
    fun protoBytesToWorkoutData(bytes: ByteArray?): WorkoutData? {
        if (bytes == null || bytes.isEmpty()) return null
        return protobuf.decodeFromByteArray(WorkoutData.serializer(), bytes)
    }
}
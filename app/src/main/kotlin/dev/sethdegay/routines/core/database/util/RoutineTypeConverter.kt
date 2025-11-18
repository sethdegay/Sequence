package dev.sethdegay.routines.core.database.util

import androidx.room.TypeConverter
import dev.sethdegay.routines.core.model.RoutineType
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.protobuf.ProtoBuf

internal class RoutineTypeConverter() {
    private val protoBuf: ProtoBuf = ProtoBuf {
        serializersModule = SerializersModule {
            polymorphic(RoutineType::class) {
                subclass(RoutineType.GENERIC::class, RoutineType.GENERIC.serializer())
                subclass(RoutineType.WORKOUT::class, RoutineType.WORKOUT.serializer())
            }
        }
    }

    @TypeConverter
    fun routineTypeToProtoBytes(value: RoutineType): ByteArray =
        protoBuf.encodeToByteArray(RoutineType.serializer(), value)

    @TypeConverter
    fun protoBytesToRoutineType(value: ByteArray): RoutineType =
        protoBuf.decodeFromByteArray(RoutineType.serializer(), value)
}

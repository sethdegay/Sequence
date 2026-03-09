package dev.sethdegay.sequence.core.database.util

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

internal class UuidByteConverter {
    @TypeConverter
    fun byteArrayToUuid(bytes: ByteArray): Uuid = Uuid.fromByteArray(bytes)

    @TypeConverter
    fun uuidToByteArray(uuid: Uuid): ByteArray = uuid.toByteArray()
}
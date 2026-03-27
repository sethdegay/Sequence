package dev.sethdegay.sequence.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.sequence.core.model.Library

data class LibraryWithSequences(
    @Embedded
    val libraryEntity: LibraryEntity,

    @Relation(
        entity = SequenceEntity::class,
        parentColumn = "id",
        entityColumn = "library_id",
    )
    val sequenceEntities: List<SequenceWithSegments>,
)

fun LibraryWithSequences.asExternalModel(): Library =
    libraryEntity.asExternalModel(sequenceEntities.map { it.asExternalModel() })
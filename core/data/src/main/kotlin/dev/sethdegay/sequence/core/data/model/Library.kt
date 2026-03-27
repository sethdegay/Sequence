package dev.sethdegay.sequence.core.data.model

import dev.sethdegay.sequence.core.database.model.LibraryEntity
import dev.sethdegay.sequence.core.model.Library

fun Library.asEntity(): LibraryEntity = LibraryEntity(
    id = id,
    title = title,
    description = description,
    dateCreated = dateCreated,
    dateModified = dateModified,
)
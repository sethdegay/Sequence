package dev.sethdegay.sequence.core.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Workspace(
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String,
    val dateCreated: Instant,
    val dateModified: Instant,
    val sequences: List<Sequence>,
)
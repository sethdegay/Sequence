package dev.sethdegay.sequence.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

typealias NavKeyInstaller = EntryProviderScope<NavKey>.() -> Unit

@Serializable
data class EditorRoute(val id: Uuid?, val workspaceId: Uuid) : NavKey

@Serializable
data object HomeRoute : NavKey

@Serializable
data object SettingsRoute : NavKey

@Serializable
data class TimerRoute(val id: Uuid) : NavKey
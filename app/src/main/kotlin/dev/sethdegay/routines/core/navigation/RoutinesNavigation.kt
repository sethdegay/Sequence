package dev.sethdegay.routines.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

typealias NavKeyInstaller = EntryProviderScope<NavKey>.() -> Unit

@Serializable
data class EditorRoute(val id: Long?) : NavKey

@Serializable
data object HomeRoute : NavKey

@Serializable
data object SettingsRoute : NavKey

@Serializable
data class TimerRoute(val id: Long) : NavKey
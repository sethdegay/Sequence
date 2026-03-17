package dev.sethdegay.sequence.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

typealias NavKeyInstaller = EntryProviderScope<NavKey>.() -> Unit
package dev.sethdegay.sequence.core.navigation

import androidx.navigation3.runtime.NavKey

interface SequenceNavigator {
    val backStack: MutableList<NavKey>
    fun navigate(key: NavKey)
    fun navigateUp()
}
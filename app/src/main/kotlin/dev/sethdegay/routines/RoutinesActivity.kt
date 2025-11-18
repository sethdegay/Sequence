package dev.sethdegay.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import dev.sethdegay.routines.core.di.RoutinesBackStackManager
import dev.sethdegay.routines.core.navigation.NavKeyInstaller
import javax.inject.Inject

@AndroidEntryPoint
class RoutinesActivity : ComponentActivity() {

    @Inject
    lateinit var backStackManager: RoutinesBackStackManager

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards NavKeyInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialExpressiveTheme {
                NavDisplay(
                    backStack = backStackManager.backStack,
                    onBack = backStackManager::navigateUp,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        entryProviderScopes.forEach { builder -> this.builder() }
                    },
                )
            }
        }
    }
}
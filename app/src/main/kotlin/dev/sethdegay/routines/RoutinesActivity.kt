package dev.sethdegay.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.di.SequenceBackStackManager
import javax.inject.Inject

@AndroidEntryPoint
class RoutinesActivity : ComponentActivity() {

    @Inject
    lateinit var backStackManager: SequenceBackStackManager

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards NavKeyInstaller>

    private val viewModel: RoutinesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val themeConfig by collectLocalThemeConfig(viewModel.uiState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.showSplashScreen() }

        setContent {
            SequenceTheme(
                darkTheme = themeConfig.darkTheme,
                dynamicColor = themeConfig.dynamicColor,
            ) {
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

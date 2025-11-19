package dev.sethdegay.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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

    private val viewModel: RoutinesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val themeConfig by collectLocalThemeConfig(viewModel.uiState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.showSplashScreen() }

        setContent {
            MaterialExpressiveTheme(
                colorScheme = when {
                    themeConfig.dynamicColor -> {
                        with(LocalContext.current) {
                            if (themeConfig.darkTheme) {
                                dynamicDarkColorScheme(this)
                            } else {
                                dynamicLightColorScheme(this)
                            }
                        }
                    }

                    themeConfig.darkTheme -> darkColorScheme()
                    else -> lightColorScheme()
                },
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

package dev.sethdegay.sequence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import dev.sethdegay.sequence.core.designsystem.theme.SequenceTheme
import dev.sethdegay.sequence.core.navigation.BottomSheetSceneStrategy
import dev.sethdegay.sequence.core.navigation.NavKeyInstaller
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import javax.inject.Inject

@AndroidEntryPoint
class SequenceActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: SequenceNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards NavKeyInstaller>

    @Inject
    lateinit var bottomSheetSceneStrategy: BottomSheetSceneStrategy<NavKey>

    private val viewModel: SequenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.showSplashScreen() }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val useDarkTheme = uiState.useDarkTheme(isSystemInDarkTheme())

            SystemBarsThemeEffect(useDarkTheme)

            SequenceTheme(
                darkTheme = useDarkTheme,
                dynamicColor = uiState.useDynamicColor,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    NavDisplay(
                        backStack = navigator.backStack,
                        onBack = navigator::navigateUp,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        entryProvider = entryProvider {
                            entryProviderScopes.forEach { builder -> this.builder() }
                        },
                        sceneStrategies = listOf(
                            bottomSheetSceneStrategy,
                            SinglePaneSceneStrategy(),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun SequenceActivity.SystemBarsThemeEffect(useDarkTheme: Boolean) {
    DisposableEffect(useDarkTheme) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = { useDarkTheme },
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = { useDarkTheme },
            ),
        )
        onDispose { }
    }
}
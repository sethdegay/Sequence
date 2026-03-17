package dev.sethdegay.sequence

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
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import javax.inject.Inject

@AndroidEntryPoint
class SequenceActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: SequenceNavigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards NavKeyInstaller>

    private val viewModel: SequenceViewModel by viewModels()

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
                    backStack = navigator.backStack,
                    onBack = navigator::navigateUp,
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

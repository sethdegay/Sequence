package dev.sethdegay.sequence

import android.content.res.Configuration
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.util.Consumer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/*
 * Observes theme changes and applies edge-to-edge styling automatically.
 */
internal fun SequenceActivity.collectLocalThemeConfig(uiState: StateFlow<SequenceUiState>): State<LocalThemeConfig> {
    val themeConfig = mutableStateOf(
        resolveLocalThemeConfig(
            isSystemInDarkTheme = resources.configuration.isSystemInDarkTheme,
            uiState = uiState.value,
        )
    )
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiState.mapToLocalThemeConfig(isSystemInDarkThemeFlow())
                .onEach { themeConfig.value = it }
                .map { it.darkTheme }
                .distinctUntilChanged()
                .collect { enableEdgeToEdge(it) }
        }
    }
    return themeConfig
}

/*
 * Determines the final LocalThemeConfig based on system state and user preference.
 */
private fun resolveLocalThemeConfig(
    isSystemInDarkTheme: Boolean,
    uiState: SequenceUiState,
): LocalThemeConfig = LocalThemeConfig(
    darkTheme = uiState.useDarkTheme(isSystemInDarkTheme),
    dynamicColor = uiState.useDynamicColor,
)

private fun Flow<SequenceUiState>.mapToLocalThemeConfig(
    isSystemInDarkThemeFlow: Flow<Boolean>,
): Flow<LocalThemeConfig> = combine(isSystemInDarkThemeFlow, this) { isSystemInDarkTheme, uiState ->
    resolveLocalThemeConfig(isSystemInDarkTheme, uiState)
}.distinctUntilChanged()

internal data class LocalThemeConfig(
    val darkTheme: Boolean,
    val dynamicColor: Boolean,
)

/*
 * Observes system configuration changes as a Flow.
 */
private fun ComponentActivity.isSystemInDarkThemeFlow(): Flow<Boolean> = callbackFlow {
    trySend(resources.configuration.isSystemInDarkTheme)

    val listener = Consumer<Configuration> { config ->
        trySend(config.isSystemInDarkTheme)
    }

    addOnConfigurationChangedListener(listener)
    awaitClose { removeOnConfigurationChangedListener(listener) }
}
    .distinctUntilChanged()
    .conflate()

private val Configuration.isSystemInDarkTheme: Boolean
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

private fun ComponentActivity.enableEdgeToEdge(darkTheme: Boolean) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = Color.TRANSPARENT,
            darkScrim = Color.TRANSPARENT,
            detectDarkMode = { darkTheme },
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
            darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b),
            detectDarkMode = { darkTheme },
        ),
    )
}
package dev.sethdegay.sequence.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val MonoLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE2E2E6),
    onPrimaryContainer = Color(0xFF1A1C1E),
    secondary = Color(0xFF5D5E62),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE1E2E6),
    onSecondaryContainer = Color(0xFF1A1C1E),
    tertiary = Color(0xFF42474E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD6DBE2),
    onTertiaryContainer = Color(0xFF000000),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFDFD),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE1E2E9),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFF74777F),
    inverseOnSurface = Color(0xFFF1F0F4),
    inverseSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFFD1D1D1),
    surfaceTint = Color(0xFF000000),
    outlineVariant = Color(0xFFC4C6D0),
    scrim = Color(0xFF000000)
)

private val MonoDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF44474F),
    onPrimaryContainer = Color(0xFFE2E2E6),
    secondary = Color(0xFFC6C6CA),
    onSecondary = Color(0xFF2E3133),
    secondaryContainer = Color(0xFF44474B),
    onSecondaryContainer = Color(0xFFE1E2E6),
    tertiary = Color(0xFFB9C3D0),
    onTertiary = Color(0xFF243140),
    tertiaryContainer = Color(0xFF3B4858),
    onTertiaryContainer = Color(0xFFD6DBE2),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF8E9199),
    inverseOnSurface = Color(0xFF1A1C1E),
    inverseSurface = Color(0xFFE2E2E6),
    inversePrimary = Color(0xFF000000),
    surfaceTint = Color(0xFFFFFFFF),
    outlineVariant = Color(0xFF44474F),
    scrim = Color(0xFF000000)
)

@Composable
fun SequenceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialExpressiveTheme(
        colorScheme = when {
            dynamicColor -> {
                with(LocalContext.current) {
                    if (darkTheme) {
                        dynamicDarkColorScheme(this)
                    } else {
                        dynamicLightColorScheme(this)
                    }
                }
            }

            darkTheme -> MonoDarkColorScheme
            else -> MonoLightColorScheme
        },
        content = content,
    )
}
package dev.sethdegay.sequence.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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

            darkTheme -> darkColorScheme()
            else -> lightColorScheme()
        },
        content = content,
    )
}
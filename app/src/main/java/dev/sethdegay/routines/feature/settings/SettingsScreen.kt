package dev.sethdegay.routines.feature.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.sethdegay.routines.feature.TestScreen

@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
) {
    TestScreen("Settings screen") {
        Button(onClick = navigateUp) {
            Text("Navigate up")
        }
    }
}
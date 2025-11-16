package dev.sethdegay.routines.feature.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.sethdegay.routines.feature.TestScreen
import kotlin.random.Random

@Composable
fun HomeScreen(
    navigateToEditor: (Long?) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToTimer: (Long) -> Unit,
) {
    TestScreen("Home screen") {
        Button(onClick = { navigateToEditor(null) }) {
            Text("Open Editor screen")
        }
        Button(onClick = navigateToSettings) {
            Text("Open Settings screen")
        }
        Button(onClick = { navigateToTimer(Random.nextLong(1, 10)) }) {
            Text("Open Timer screen")
        }
    }
}
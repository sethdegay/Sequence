package dev.sethdegay.routines.feature.timer

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.sethdegay.routines.feature.TestScreen

@Composable
fun TimerScreen(
    id: Long,
    navigateUp: () -> Unit,
) {
    TestScreen("Timer screen | ID: $id") {
        Button(onClick = navigateUp) {
            Text("Navigate up")
        }
    }
}
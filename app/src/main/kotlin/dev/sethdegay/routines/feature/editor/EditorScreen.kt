package dev.sethdegay.routines.feature.editor

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.sethdegay.routines.feature.TestScreen

@Composable
fun EditorScreen(
    id: Long?,
    navigateUp: () -> Unit,
) {
    TestScreen("Editor screen | ID: $id") {
        Button(onClick = navigateUp) {
            Text("Navigate up")
        }
    }
}
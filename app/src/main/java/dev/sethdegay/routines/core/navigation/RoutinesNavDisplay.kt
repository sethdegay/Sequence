package dev.sethdegay.routines.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun RoutinesNavDisplay(
    modifier: Modifier = Modifier,
    startDestination: NavKey = HomeRoute,
) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) { key ->
        when (key) {
            is HomeRoute -> NavEntry(key) {
                TestScreen(
                    message = "Home screen",
                    additionalContent = {
                        Button(onClick = { backStack.add(TestRoute("Test screen")) }) {
                            Text("Open Test screen")
                        }
                    }
                )
            }

            is TestRoute -> NavEntry(key) {
                TestScreen(key.message)
            }

            else -> {
                error("Unknown key: $key")
            }
        }
    }
}

@Composable
private fun TestScreen(message: String, additionalContent: (@Composable () -> Unit)? = null) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(message)
            Spacer(modifier = Modifier.size(16.dp))
            additionalContent?.invoke()
        }
    }
}
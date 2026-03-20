package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun LoadingBox(
    modifier: Modifier = Modifier,
    indicatorSize: Dp,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = { LoadingIndicator(modifier = Modifier.size(indicatorSize)) },
    )
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    LoadingBox(
        modifier = modifier.fillMaxSize(),
        indicatorSize = 162.dp,
    )
}

@Composable
fun LoadingSection() {
    LoadingBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        indicatorSize = 64.dp,
    )
}
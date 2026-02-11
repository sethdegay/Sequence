package dev.sethdegay.sequence.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ProgressIndicatorAmplitudeLevel(val value: Float) { FLAT(0f), MODERATE(0.5f), MAXIMUM(1f); }

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    expanded: Boolean,
    amplitudeLevel: ProgressIndicatorAmplitudeLevel,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1_000,
            easing = LinearEasing,
        ),
    )
    val thickStrokeWidth = with(LocalDensity.current) { 8.dp.toPx() }
    val thickStroke = remember(thickStrokeWidth) {
        Stroke(
            width = thickStrokeWidth,
            cap = StrokeCap.Round,
        )
    }
    LinearWavyProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .height(if (expanded) 14.dp else 10.dp),
        progress = { animatedProgress },
        amplitude = { amplitudeLevel.value },
        stroke = if (expanded) thickStroke else WavyProgressIndicatorDefaults.linearIndicatorStroke,
        trackStroke = if (expanded) thickStroke else WavyProgressIndicatorDefaults.linearTrackStroke,
    )
}

@Preview(showBackground = true)
@Composable
private fun ProgressIndicatorPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProgressIndicator(
            progress = 0f,
            expanded = true,
            amplitudeLevel = ProgressIndicatorAmplitudeLevel.FLAT,
        )
        ProgressIndicator(
            progress = 0.33f,
            expanded = true,
            amplitudeLevel = ProgressIndicatorAmplitudeLevel.FLAT,
        )
        ProgressIndicator(
            progress = 0.66f,
            expanded = true,
            amplitudeLevel = ProgressIndicatorAmplitudeLevel.MODERATE,
        )
        ProgressIndicator(
            progress = 0.99f,
            expanded = true,
            amplitudeLevel = ProgressIndicatorAmplitudeLevel.MAXIMUM,
        )
    }
}
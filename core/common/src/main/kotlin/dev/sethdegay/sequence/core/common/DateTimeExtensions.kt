package dev.sethdegay.sequence.core.common

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Instant.toDateTimeString(formatStyle: FormatStyle = FormatStyle.MEDIUM): String {
    val formatter = DateTimeFormatter
        .ofLocalizedDateTime(formatStyle)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this.toJavaInstant())
}

fun Instant.toTimeString(formatStyle: FormatStyle = FormatStyle.MEDIUM): String {
    val formatter = DateTimeFormatter
        .ofLocalizedTime(formatStyle)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    return formatter.format(this.toJavaInstant())
}
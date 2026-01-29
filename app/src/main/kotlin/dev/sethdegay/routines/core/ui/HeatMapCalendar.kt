package dev.sethdegay.routines.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.HeatMapWeek
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import dev.sethdegay.routines.R.string
import dev.sethdegay.routines.core.model.HeatMapLevel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private val HeatMapLevel.dayColor: Color
    @Composable
    get() = when (this) {
        HeatMapLevel.Zero -> ButtonDefaults.buttonColors().containerColor.copy(alpha = 0.05f)
        HeatMapLevel.One -> ButtonDefaults.buttonColors().containerColor.copy(alpha = 0.25f)
        HeatMapLevel.Two -> ButtonDefaults.buttonColors().containerColor.copy(alpha = 0.50f)
        HeatMapLevel.Three -> ButtonDefaults.buttonColors().containerColor.copy(alpha = 0.75f)
        HeatMapLevel.Four -> ButtonDefaults.buttonColors().containerColor.copy(alpha = 1.0f)
    }
private val defaultDaySize = 32.dp
private val smallDaySize = 16.dp

@Composable
fun HeatMapCalendar(
    modifier: Modifier = Modifier,
    heatMapData: Map<LocalDate, HeatMapLevel>,
    start: LocalDate,
    end: LocalDate,
    onDateClicked: (LocalDate) -> Unit,
) {
    val state = rememberHeatMapCalendarState(
        startMonth = start.yearMonth,
        endMonth = end.yearMonth,
        firstVisibleMonth = end.yearMonth,
        firstDayOfWeek = firstDayOfWeekFromLocale(),
    )
    Column(modifier = modifier) {
        HeatMapCalendar(
            state = state,
            dayContent = { day, week ->
                Day(
                    day = day,
                    startDate = start,
                    endDate = end,
                    week = week,
                    heatMapLevel = heatMapData[day.date] ?: HeatMapLevel.Zero,
                    onClick = onDateClicked,
                )
            },
            weekHeader = { WeekHeader(it) },
            monthHeader = { MonthHeader(it, end) },
            userScrollEnabled = true,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Legend(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun Day(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    startDate: LocalDate,
    endDate: LocalDate,
    week: HeatMapWeek,
    heatMapLevel: HeatMapLevel,
    onClick: (LocalDate) -> Unit,
) {
    val weekDates = week.days.map { it.date }
    if (day.date in startDate..endDate) {
        LevelBox(
            modifier = modifier,
            color = heatMapLevel.dayColor,
            onClick = if (heatMapLevel != HeatMapLevel.Zero) {
                { onClick(day.date) }
            } else {
                null
            },
        )
    } else if (weekDates.contains(startDate)) {
        LevelBox(
            modifier = modifier,
            color = Color.Transparent,
        )
    }
}

@Composable
private fun LevelBox(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (() -> Unit)? = null,
    size: Dp = defaultDaySize,
) {
    Box(
        modifier = modifier
            .size(size)
            .padding(2.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color = color)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
    )
}

@Composable
private fun WeekHeader(dayOfWeek: DayOfWeek) {
    Box(
        modifier = Modifier
            .height(defaultDaySize)
            .padding(end = 8.dp),
    ) {
        Text(
            text = dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault(),
            ),
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun MonthHeader(
    calendarMonth: CalendarMonth,
    endDate: LocalDate,
) {
    if (calendarMonth.weekDays.first().first().date <= endDate) {
        val month = calendarMonth.yearMonth
        val title = month.month.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 1.dp, start = 2.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun Legend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(string.heat_map_calendar_legend_less_text),
            style = MaterialTheme.typography.labelSmall,
        )
        Spacer(modifier = Modifier.size(8.dp))
        HeatMapLevel.entries.forEach { heatMapLevel ->
            LevelBox(
                color = heatMapLevel.dayColor,
                size = smallDaySize,
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(string.heat_map_calendar_legend_more_text),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
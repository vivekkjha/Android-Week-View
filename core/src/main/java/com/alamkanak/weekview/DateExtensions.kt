package com.alamkanak.weekview

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

internal fun LocalDate.withTimeAtStartOfPeriod(hour: Int) = atStartOfDay().withTimeAtStartOfPeriod(hour)

internal fun LocalDateTime.withTimeAtStartOfPeriod(hour: Int): LocalDateTime {
    return withHour(hour).truncatedTo(ChronoUnit.HOURS)
}

internal fun LocalDate.withTimeAtEndOfPeriod(hour: Int) = atStartOfDay().withTimeAtEndOfPeriod(hour)

internal fun LocalDateTime.withTimeAtEndOfPeriod(hour: Int): LocalDateTime {
    return withHour(hour - 1).withMinute(59).withSecond(59).withNano(999_999_999)
}

internal val LocalDate.daysFromToday: Int
    get() = ChronoUnit.DAYS.between(LocalDate.now(), this).toInt()

internal fun createDateRange(start: Int, end: Int): List<LocalDate> {
    val firstDate = LocalDate.now()
    return (start..end).map { firstDate.plusDays(it - 1) }
}

internal fun LocalDateTime.isNextDayAfter(other: LocalDateTime): Boolean {
    return ChronoUnit.DAYS.between(other.toLocalDate(), this.toLocalDate()) == 1L
}

internal val LocalDateTime.isAtStartOfDay: Boolean
    get() = isEqual(toLocalDate().atStartOfDay())

internal fun defaultDateFormatter(
    numberOfDays: Int
): DateTimeFormatter = when (numberOfDays) {
    1 -> DateTimeFormatter.ofPattern("EEEE M/dd", Locale.getDefault()) // full weekday
    in 2..6 -> DateTimeFormatter.ofPattern("EEE M/dd", Locale.getDefault()) // first three characters
    else -> DateTimeFormatter.ofPattern("EEEEE M/dd", Locale.getDefault()) // first character
}

internal fun defaultTimeFormatter(): DateTimeFormatter = DateTimeFormatter.ofPattern("hh a", Locale.getDefault())

internal fun LocalDateTime.limitedBy(minHour: Int, maxHour: Int): LocalDateTime {
    return when {
        hour < minHour -> withHour(hour)
        hour > maxHour -> withHour(hour - 1).withMinute(59).withSecond(59).withNano(999_999_999)
        else -> this
    }
}

internal fun LocalDate.plusDays(days: Int) = plusDays(days.toLong())

internal fun LocalDate.minusDays(days: Int) = minusDays(days.toLong())

internal fun LocalDate.isNotEqual(other: LocalDate) = !isEqual(other)

internal val LocalDate.isWeekend: Boolean
    get() = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY

internal val LocalDate.isBeforeToday: Boolean
    get() = isBefore(LocalDate.now())

internal val LocalDate.isToday: Boolean
    get() = isEqual(LocalDate.now())

internal fun LocalDateTime.minusMinutes(minutes: Int) = minusMinutes(minutes.toLong())

internal fun Calendar.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
}

internal fun LocalDate.toCalendar(): Calendar {
    val instant = atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
    val calendar = Calendar.getInstance()
    calendar.time = Date.from(instant)
    return calendar
}

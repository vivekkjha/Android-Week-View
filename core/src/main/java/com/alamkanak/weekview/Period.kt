package com.alamkanak.weekview

import java.time.LocalDate
import java.time.Month

internal data class FetchRange(
    val previous: Period,
    val current: Period,
    val next: Period
) {

    val periods: List<Period> = listOf(previous, current, next)

    internal companion object {
        fun create(firstVisibleDay: LocalDate): FetchRange {
            val current = Period.fromDate(firstVisibleDay)
            return FetchRange(current.previous, current, current.next)
        }
    }
}

internal data class Period(
    val month: Month,
    val year: Int
) : Comparable<Period> {

    val previous: Period
        get() {
            val year = if (month == Month.JANUARY) year - 1 else year
            val month = if (month == Month.JANUARY) Month.DECEMBER else month - 1
            return Period(month, year)
        }

    val next: Period
        get() {
            val year = if (month == Month.DECEMBER) year + 1 else year
            val month = if (month == Month.DECEMBER) Month.JANUARY else month + 1
            return Period(month, year)
        }

    val startDate: LocalDate = LocalDate.of(year, month, 1)
    val endDate: LocalDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

    override fun compareTo(other: Period): Int {
        return when {
            year < other.year -> -1
            year > other.year -> 1
            else -> month.compareTo(other.month)
        }
    }

    internal companion object {
        fun fromDate(date: LocalDate): Period = Period(month = date.month, year = date.year)
    }
}

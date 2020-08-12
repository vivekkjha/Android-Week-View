package com.alamkanak.weekview

import java.time.LocalDate
import java.time.Month
import org.junit.Assert.assertEquals
import org.junit.Test

class PeriodTest {

    private val firstDayOfYear = LocalDate.now().withMonth(Month.JANUARY.value)

    @Test
    fun `returns correct previous period for January`() {
        val period = Period.fromDate(firstDayOfYear)
        val previous = period.previous

        assertEquals(previous.month, Month.DECEMBER)
        assertEquals(previous.year, period.year - 1)
    }

    @Test
    fun `returns correct previous period for August`() {
        val date = firstDayOfYear.withMonth(Month.AUGUST.value)
        val period = Period.fromDate(date)
        val previous = period.previous

        assertEquals(previous.month, Month.JULY)
        assertEquals(previous.year, period.year)
    }

    @Test
    fun `returns correct next period for August`() {
        val date = firstDayOfYear.withMonth(Month.AUGUST.value)
        val period = Period.fromDate(date)
        val next = period.next

        assertEquals(next.month, Month.SEPTEMBER)
        assertEquals(next.year, period.year)
    }

    @Test
    fun `returns correct next period for December`() {
        val date = firstDayOfYear.withMonth(Month.DECEMBER.value)
        val period = Period.fromDate(date)
        val next = period.next

        assertEquals(next.month, Month.JANUARY)
        assertEquals(next.year, period.year + 1)
    }
}

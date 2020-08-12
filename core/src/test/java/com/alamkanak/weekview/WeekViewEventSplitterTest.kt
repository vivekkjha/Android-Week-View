package com.alamkanak.weekview

import com.alamkanak.weekview.model.Event
import com.alamkanak.weekview.util.createResolvedWeekViewEvent
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class WeekViewEventSplitterTest {

    private val viewState = mock(ViewState::class.java)

    init {
        MockitoAnnotations.initMocks(this)
        whenever(viewState.minHour).thenReturn(0)
        whenever(viewState.maxHour).thenReturn(24)
    }

    @Test
    fun `single-day event is not split`() {
        val startTime = LocalDate.now().atTime(11, 0)
        val endTime = startTime.plusHours(2)
        val event = createResolvedWeekViewEvent(startTime, endTime)

        val results = event.split(viewState)
        val expected = listOf(event)

        assertEquals(expected, results)
    }

    @Test
    fun `two-day event is split correctly`() {
        val startTime = LocalDate.now().atTime(11, 0)
        val endTime = (startTime.plusDays(1)).withHour(2)

        val event = createResolvedWeekViewEvent(startTime, endTime)
        val results = event.split(viewState)

        val expected = listOf(
            Event(startTime, startTime.withTimeAtEndOfPeriod(24)),
            Event(endTime.withTimeAtStartOfPeriod(0), endTime)
        )

        assertEqualEvents(expected, results)
    }

    @Test
    fun `three-day event is split correctly`() {
        val startTime = LocalDate.now().atTime(11, 0)
        val endTime = (startTime.plusDays(2)).withHour(2)

        val event = createResolvedWeekViewEvent(startTime, endTime)
        val results = event.split(viewState)

        val intermediateDate = startTime.plusDays(1)
        val expected = listOf(
            Event(startTime, startTime.withTimeAtEndOfPeriod(hour = 24)),
            Event(intermediateDate.withTimeAtStartOfPeriod(0), intermediateDate.withTimeAtEndOfPeriod(24)),
            Event(endTime.withTimeAtStartOfPeriod(0), endTime)
        )

        assertEqualEvents(expected, results)
    }

    private fun assertEqualEvents(expected: List<Event>, received: List<ResolvedWeekViewEvent<*>>) {
        val zipped = expected.zip(received)
        val isEqual = zipped.all { (expected, result) ->
            expected.startTime.isEqual(result.startTime) && expected.endTime.isEqual(result.endTime)
        }
        assertTrue(isEqual)
    }
}

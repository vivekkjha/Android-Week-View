package com.alamkanak.weekview.util

import com.alamkanak.weekview.ResolvedWeekViewEvent
import java.time.LocalDateTime

internal fun createResolvedWeekViewEvent(
    startTime: LocalDateTime,
    endTime: LocalDateTime
): ResolvedWeekViewEvent<Unit> {
    return ResolvedWeekViewEvent(
        id = 0,
        title = "Title",
        startTime = startTime,
        endTime = endTime,
        location = null,
        isAllDay = false,
        style = ResolvedWeekViewEvent.Style(
            backgroundColor = 0,
            borderColor = 0,
            borderWidth = 0,
            textColor = 0,
            isTextStrikeThrough = false
        ),
        data = Unit
    )
}

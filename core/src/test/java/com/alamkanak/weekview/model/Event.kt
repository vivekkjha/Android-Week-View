package com.alamkanak.weekview.model

import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import java.time.LocalDateTime

data class Event(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) : WeekViewDisplayable<Event> {

    override fun toWeekViewEvent(): WeekViewEvent<Event> {
        return WeekViewEvent.Builder(this)
            .setId(1)
            .setTitle("")
            .setStartTime(startTime)
            .setEndTime(endTime)
            .build()
    }
}

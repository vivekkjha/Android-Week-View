package com.alamkanak.weekview.sample.data.model

import android.graphics.Color
import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * An event model that was built for automatic serialization from json to object.
 * Created by Raquib-ul-Alam Kanak on 1/3/16.
 * Website: http://alamkanak.github.io
 */
data class ApiEvent(
    @Expose
    @SerializedName("name")
    var title: String,
    @Expose
    @SerializedName("dayOfMonth")
    var dayOfMonth: Int,
    @Expose
    @SerializedName("startTime")
    var startTime: String,
    @Expose
    @SerializedName("endTime")
    var endTime: String,
    @Expose
    @SerializedName("color")
    var color: String
) : WeekViewDisplayable<ApiEvent> {

    override fun toWeekViewEvent(): WeekViewEvent<ApiEvent> {
        // Titles have the format "Event 123"
        val id = title.split(" ").last().toLong()

        val (startHour, startMinute) = startTime.split(":").map { it.toInt() }
        val (endHour, endMinute) = endTime.split(":").map { it.toInt() }

        val startTime = LocalDateTime.now()
            .withDayOfMonth(dayOfMonth)
            .withHour(startHour)
            .withMinute(startMinute)
            .truncatedTo(ChronoUnit.MINUTES)

        val endTime = LocalDateTime.now()
            .withDayOfMonth(dayOfMonth)
            .withHour(endHour)
            .withMinute(endMinute)
            .truncatedTo(ChronoUnit.MINUTES)

        val color = Color.parseColor(color)
        val style = WeekViewEvent.Style.Builder()
            .setBackgroundColor(color)
            .build()

        return WeekViewEvent.Builder(this)
            .setId(id)
            .setTitle(title)
            .setStartTime(startTime)
            .setEndTime(endTime)
            .setAllDay(false)
            .setStyle(style)
            .build()
    }
}

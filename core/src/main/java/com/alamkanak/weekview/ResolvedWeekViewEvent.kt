package com.alamkanak.weekview

import android.content.Context
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

internal fun <T> WeekViewDisplayable<T>.toResolvedWeekViewEvent(
    context: Context
) = toWeekViewEvent().resolve(context)

internal fun <T> WeekViewEvent<T>.resolve(
    context: Context
): ResolvedWeekViewEvent<T> {
    return ResolvedWeekViewEvent(
        id = id,
        title = titleResource.resolve(context, shouldSetBold = true),
        startTime = startTime,
        endTime = endTime,
        location = locationResource?.resolve(context, shouldSetBold = false),
        isAllDay = isAllDay,
        style = style.resolve(context),
        data = data
    )
}

internal fun WeekViewEvent.Style.resolve(
    context: Context
) = ResolvedWeekViewEvent.Style(
    backgroundColor = backgroundColorResource?.resolve(context),
    borderColor = borderColorResource?.resolve(context),
    borderWidth = borderWidthResource?.resolve(context),
    textColor = textColorResource?.resolve(context),
    isTextStrikeThrough = isTextStrikeThrough
)

internal data class ResolvedWeekViewEvent<T>(
    val id: Long,
    val title: CharSequence,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: CharSequence?,
    val isAllDay: Boolean,
    val style: Style,
    val data: T
) {

    data class Style(
        val backgroundColor: Int? = null,
        val borderColor: Int? = null,
        val borderWidth: Int? = null,
        val textColor: Int? = null,
        @Deprecated("No longer used.")
        val isTextStrikeThrough: Boolean = false
    )

    internal val isNotAllDay: Boolean = isAllDay.not()

    internal val durationInMinutes: Int = ChronoUnit.MINUTES.between(startTime, endTime).toInt()

    internal val isMultiDay: Boolean = startTime.toLocalDate().isNotEqual(endTime.toLocalDate())

    internal fun isWithin(
        minHour: Int,
        maxHour: Int
    ): Boolean = startTime.hour >= minHour && endTime.hour <= maxHour

    internal fun collidesWith(other: ResolvedWeekViewEvent<*>): Boolean {
        if (isAllDay != other.isAllDay) {
            return false
        }

        if (startTime.isEqual(other.startTime) && endTime.isEqual(other.endTime)) {
            // Complete overlap
            return true
        }

        // Resolve collisions by shortening the preceding event by 1 ms
        if (endTime.isEqual(other.startTime)) {
            // TODO endTime -= Millis(1)
            return false
        } else if (startTime.isEqual(other.endTime)) {
            // TODO other.endTime -= Millis(1)
        }

        return !startTime.isAfter(other.endTime) && !endTime.isBefore(other.startTime)
    }

    internal fun startsOnEarlierDay(
        originalEvent: ResolvedWeekViewEvent<*>
    ): Boolean = startTime.toLocalDate().isNotEqual(originalEvent.startTime.toLocalDate())

    internal fun endsOnLaterDay(
        originalEvent: ResolvedWeekViewEvent<*>
    ): Boolean = endTime.toLocalDate().isNotEqual(originalEvent.endTime.toLocalDate())
}

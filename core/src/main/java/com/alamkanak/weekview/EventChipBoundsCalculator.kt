package com.alamkanak.weekview

import android.graphics.RectF

internal class EventChipBoundsCalculator(
    private val viewState: ViewState
) {

    fun calculateSingleEvent(
        eventChip: EventChip,
        startPixel: Float
    ): RectF {
        val widthPerDay = viewState.widthPerDay

        val minutesFromStart = eventChip.minutesFromStartHour
        val top = calculateDistanceFromTop(minutesFromStart)

        val bottomMinutesFromStart = minutesFromStart + eventChip.event.durationInMinutes
        val bottom = calculateDistanceFromTop(bottomMinutesFromStart) - viewState.eventMarginVertical

        var left = startPixel + eventChip.relativeStart * widthPerDay
        var right = left + eventChip.relativeWidth * widthPerDay

        if (left > startPixel) {
            left += viewState.overlappingEventGap / 2
        }

        if (right < startPixel + widthPerDay) {
            right -= viewState.overlappingEventGap / 2
        }

        val hasNoOverlaps = (right == startPixel + widthPerDay)
        if (viewState.isSingleDay && hasNoOverlaps) {
            right -= viewState.eventMarginHorizontal * 2
        }

        return RectF(left, top, right, bottom)
    }

    private fun calculateDistanceFromTop(
        minutesFromStart: Int
    ): Float = with(viewState) {
        val portionOfDay = minutesFromStart.toFloat() / minutesPerDay
        val pixelsFromTop = hourHeight * hoursPerDay * portionOfDay
        return pixelsFromTop + currentOrigin.y + headerHeight
    }

    fun calculateAllDayEvent(
        eventChip: EventChip,
        startPixel: Float
    ): RectF {
        val padding = viewState.headerRowPadding

        val top = padding + viewState.dateLabelHeight + padding
        val height = viewState.allDayEventTextPaint.textSize + viewState.eventPaddingVertical * 2
        val bottom = top + height

        val widthPerDay = viewState.widthPerDay

        var left = startPixel + eventChip.relativeStart * widthPerDay
        var right = left + eventChip.relativeWidth * widthPerDay

        if (left > startPixel) {
            left += viewState.overlappingEventGap / 2f
        }

        if (right < startPixel + widthPerDay) {
            right -= viewState.overlappingEventGap / 2f
        }

        val hasNoOverlaps = (right == startPixel + widthPerDay)
        if (viewState.isSingleDay && hasNoOverlaps) {
            right -= viewState.eventMarginHorizontal * 2
        }

        return RectF(left, top, right, bottom)
    }
}

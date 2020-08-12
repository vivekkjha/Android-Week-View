package com.alamkanak.weekview

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

internal class EventChipsCache {

    val allEventChips: List<EventChip>
        get() = normalEventChipsByDate.values.flatten() + allDayEventChipsByDate.values.flatten()

    private val normalEventChipsByDate = ConcurrentHashMap<Long, MutableList<EventChip>>()
    private val allDayEventChipsByDate = ConcurrentHashMap<Long, MutableList<EventChip>>()

    fun allEventChipsInDateRange(
        dateRange: List<LocalDate>
    ): List<EventChip> {
        val results = mutableListOf<EventChip>()
        for (date in dateRange) {
            results += allDayEventChipsByDate[date.toEpochDay()].orEmpty()
            results += normalEventChipsByDate[date.toEpochDay()].orEmpty()
        }
        return results
    }

    fun normalEventChipsByDate(
        date: LocalDate
    ): List<EventChip> = normalEventChipsByDate[date.toEpochDay()].orEmpty()

    fun allDayEventChipsByDate(
        date: LocalDate
    ): List<EventChip> = allDayEventChipsByDate[date.toEpochDay()].orEmpty()

    fun allDayEventChipsInDateRange(
        dateRange: List<LocalDate>
    ): List<EventChip> {
        val results = mutableListOf<EventChip>()
        for (date in dateRange) {
            results += allDayEventChipsByDate[date.toEpochDay()].orEmpty()
        }
        return results
    }

    private fun put(newChips: List<EventChip>) {
        for (eventChip in newChips) {
            val key = eventChip.event.startTime.toLocalDate().toEpochDay()
            if (eventChip.event.isAllDay) {
                allDayEventChipsByDate.addOrReplace(key, eventChip)
            } else {
                normalEventChipsByDate.addOrReplace(key, eventChip)
            }
        }
    }

    operator fun plusAssign(newChips: List<EventChip>) = put(newChips)

    fun clearSingleEventsCache() {
        allEventChips.filter { it.originalEvent.isNotAllDay }.forEach(EventChip::clearCache)
    }

    fun clear() {
        allDayEventChipsByDate.clear()
        normalEventChipsByDate.clear()
    }

    private fun ConcurrentHashMap<Long, MutableList<EventChip>>.addOrReplace(
        key: Long,
        eventChip: EventChip
    ) {
        val results = getOrElse(key) { mutableListOf() }
        val indexOfExisting = results.indexOfFirst { it.event.id == eventChip.event.id }
        if (indexOfExisting != -1) {
            // If an event with the same ID already exists, replace it. The new event will likely be
            // more up-to-date.
            results.removeAt(indexOfExisting)
            results.add(indexOfExisting, eventChip)
        } else {
            results.add(eventChip)
        }

        this[key] = results
    }
}

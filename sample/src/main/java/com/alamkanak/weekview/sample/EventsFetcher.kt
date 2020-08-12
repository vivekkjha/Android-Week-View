package com.alamkanak.weekview.sample

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.sample.data.EventsDatabase
import com.alamkanak.weekview.sample.data.model.Event
import java.time.LocalDate

internal class EventsFetcher(context: Context) {

    private val database = EventsDatabase(context)

    fun fetch(
        startDate: LocalDate,
        endDate: LocalDate,
        onResult: (List<WeekViewDisplayable<Event>>) -> Unit
    ) {
        val thread = HandlerThread("events-fetcher")
        thread.start()

        val looper = thread.looper
        val handler = Handler(looper)

        handler.post {
            val events = database.getEventsInRange(startDate.toCalendar(), endDate.toCalendar())
            onResult(events)
        }
    }
}

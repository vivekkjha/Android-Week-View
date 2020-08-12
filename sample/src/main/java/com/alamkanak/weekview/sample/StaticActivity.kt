package com.alamkanak.weekview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.sample.data.model.Event
import com.alamkanak.weekview.sample.util.setupWithWeekView
import com.alamkanak.weekview.sample.util.showToast
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle.LONG
import java.time.format.FormatStyle.SHORT
import kotlinx.android.synthetic.main.activity_static.dateRangeTextView
import kotlinx.android.synthetic.main.activity_static.nextWeekButton
import kotlinx.android.synthetic.main.activity_static.previousWeekButton
import kotlinx.android.synthetic.main.activity_static.weekView
import kotlinx.android.synthetic.main.view_toolbar.toolbar

class StaticActivity : AppCompatActivity() {

    private val eventsFetcher: EventsFetcher by lazy { EventsFetcher(this) }

    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(LONG)

    private val adapter: StaticActivityWeekViewAdapter by lazy {
        StaticActivityWeekViewAdapter(
            loadMoreHandler = this::onLoadMore,
            rangeChangeHandler = this::onRangeChanged
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static)

        toolbar.setupWithWeekView(weekView)
        weekView.adapter = adapter

        previousWeekButton.setOnClickListener {
            weekView.goToDate(weekView.firstVisibleDate.minusDays(7))
        }

        nextWeekButton.setOnClickListener {
            weekView.goToDate(weekView.firstVisibleDate.plusDays(7))
        }
    }

    private fun onLoadMore(startDate: LocalDate, endDate: LocalDate) {
        eventsFetcher.fetch(startDate, endDate, adapter::submit)
    }

    private fun onRangeChanged(startDate: LocalDate, endDate: LocalDate) {
        val formattedFirstDay = dateFormatter.format(startDate)
        val formattedLastDay = dateFormatter.format(endDate)
        dateRangeTextView.text = getString(R.string.date_infos, formattedFirstDay, formattedLastDay)
    }
}

private class StaticActivityWeekViewAdapter(
    private val rangeChangeHandler: (startDate: LocalDate, endDate: LocalDate) -> Unit,
    private val loadMoreHandler: (startDate: LocalDate, endDate: LocalDate) -> Unit
) : WeekView.PagingAdapter<Event>() {

    private val formatter = DateTimeFormatter.ofLocalizedDateTime(LONG, SHORT)

    override fun onEventClick(data: Event) {
        context.showToast("Removed ${data.title}")
    }

    override fun onEmptyViewClick(time: LocalDateTime) {
        context.showToast("Empty view clicked at ${formatter.format(time)}")
    }

    override fun onEventLongClick(data: Event) {
        context.showToast("Long-clicked ${data.title}")
    }

    override fun onEmptyViewLongClick(time: LocalDateTime) {
        context.showToast("Empty view long-clicked at ${formatter.format(time)}")
    }

    override fun onLoadMore(startDate: LocalDate, endDate: LocalDate) {
        loadMoreHandler(startDate, endDate)
    }

    override fun onRangeChanged(firstVisibleDate: LocalDate, lastVisibleDate: LocalDate) {
        rangeChangeHandler(firstVisibleDate, lastVisibleDate)
    }
}

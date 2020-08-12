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
import kotlinx.android.synthetic.main.activity_limited.weekView
import kotlinx.android.synthetic.main.view_toolbar.toolbar

class LimitedActivity : AppCompatActivity() {

    private val eventsFetcher: EventsFetcher by lazy { EventsFetcher(this) }

    private val adapter: LimitedActivityWeekViewAdapter by lazy {
        LimitedActivityWeekViewAdapter(loadMoreHandler = this::onLoadMore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limited)

        toolbar.setupWithWeekView(weekView)
        setupDateRange()

        weekView.adapter = adapter
    }

    private fun setupDateRange() {
        weekView.minDate = LocalDate.now().withDayOfMonth(1)
        weekView.maxDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
    }

    private fun onLoadMore(startDate: LocalDate, endDate: LocalDate) {
        eventsFetcher.fetch(startDate, endDate, adapter::submit)
    }
}

private class LimitedActivityWeekViewAdapter(
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
}

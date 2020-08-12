package com.alamkanak.weekview.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.sample.data.EventsDatabase
import com.alamkanak.weekview.sample.data.model.Event
import com.alamkanak.weekview.sample.util.lazyView
import com.alamkanak.weekview.sample.util.setupWithWeekView
import com.google.android.material.appbar.MaterialToolbar
import java.time.LocalDate
import kotlinx.android.synthetic.main.fragment_week.weekView

class WithFragmentActivity : AppCompatActivity(R.layout.activity_with_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, WeekFragment.newInstance())
                .commit()
        }
    }
}

class WeekFragment : Fragment(R.layout.fragment_week) {

    private val toolbar: MaterialToolbar by lazyView(R.id.toolbar)
    private val database: EventsDatabase by lazy { EventsDatabase(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.setupWithWeekView(weekView)

        val start = LocalDate.now().withDayOfMonth(1)
        val end = start.withDayOfMonth(start.lengthOfMonth())

        val adapter = WeekView.SimpleAdapter<Event>()
        weekView.adapter = adapter

        val events = database.getEventsInRange(start.toCalendar(), end.toCalendar())
        adapter.submit(events)

        // Limit WeekView to the current month
        weekView.minDate = start
        weekView.maxDate = end
    }

    companion object {
        fun newInstance() = WeekFragment()
    }
}

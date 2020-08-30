package com.alamkanak.weekview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class WeekViewTestActivity : AppCompatActivity() {

    private lateinit var weekView: WeekView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weekView = WeekView(this)
        setContentView(weekView)
    }

    internal fun updateWeekView(block: WeekView.() -> Unit) {
        weekView.block()
    }
}

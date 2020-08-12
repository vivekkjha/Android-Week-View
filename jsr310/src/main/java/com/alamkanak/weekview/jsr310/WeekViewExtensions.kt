package com.alamkanak.weekview.jsr310

// fun <T : Any> WeekViewEvent.Builder<T>.setStartTime(startTime: LocalDateTime): WeekViewEvent.Builder<T> {
//    return setStartTime(startTime.toCalendar())
// }
//
// fun <T : Any> WeekViewEvent.Builder<T>.setEndTime(endTime: LocalDateTime): WeekViewEvent.Builder<T> {
//    return setEndTime(endTime.toCalendar())
// }
//
// val WeekView.jsr310Adapter: WeekViewJsr310Adapter
//    get() = WeekViewJsr310Adapter(this)
//
// fun WeekView.goToDate(date: LocalDate) {
//    goToDate(date.toCalendar())
// }
//
// fun WeekView.setDateFormatter(formatter: (LocalDate) -> String) {
//    setDateFormatter { formatter(it.toLocalDate()) }
// }

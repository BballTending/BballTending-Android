package com.bballtending.android.domain.calendar.model

data class CalendarMonthData(
    val year: Int,
    val month: Int,
    val day2DList: List<List<CalendarDayData>>
)
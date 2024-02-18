package com.bballtending.android.domain.calendar.model

import java.time.LocalDate

object CalendarConfig {
    private const val startYear: Int = 2000
    val yearRange: IntRange = IntRange(startYear, LocalDate.now().year + 100)
}

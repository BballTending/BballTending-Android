package com.bballtending.android.domain.calendar.model

object DayOfWeek {
    private val enums: Array<DayOfWeek> = DayOfWeek.values()
    
    fun getDisplayName(): List<String> {
        return arrayListOf<String>().apply {
            enums.forEach { enum ->
                val str = when (enum) {
                    DayOfWeek.SUNDAY -> "일"
                    DayOfWeek.MONDAY -> "월"
                    DayOfWeek.TUESDAY -> "화"
                    DayOfWeek.WEDNESDAY -> "수"
                    DayOfWeek.THURSDAY -> "목"
                    DayOfWeek.FRIDAY -> "금"
                    DayOfWeek.SATURDAY -> "토"
                }
                add(str)
            }
        }
    }

    enum class DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
}
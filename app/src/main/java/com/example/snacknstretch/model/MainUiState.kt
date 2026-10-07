package com.example.snacknstretch.model

data class MainUiState(
    val workingTime: WorkingTime = WorkingTime.FIFTEEN,
    val breakTime: BreakTime = BreakTime.FIVE,
    val isBatteryLowSwitched: Boolean = false,
    val isChargingSwitched: Boolean = false,
    val text: String = "",
    val isLoading: Boolean = false
)

enum class WorkingTime(val time: Int) {
    FIFTEEN(15),
    THIRTY(30),
    FORTY_FIVE(45),
    SIXTY(60)
}

enum class BreakTime(val time: Int) {
    FIVE(5),
    TEN(10),
    FIFTEEN(15),
    TWENTY(20)
}
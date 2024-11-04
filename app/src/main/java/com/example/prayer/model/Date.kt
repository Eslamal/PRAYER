package com.example.prayer.model

data class Date(
    val gregorian: Gregorian,
    val readable: String,
    val timestamp: String
)
data class Gregorian(
    val date: String,
    val day: String,
    //val designation: Designation,
    val format: String,
    val month: MonthName,
    val weekday: Weekday,
    val year: String
)
data class MonthName(
    val en: String,
    val number: Int
)
data class Weekday(
    val en: String
)
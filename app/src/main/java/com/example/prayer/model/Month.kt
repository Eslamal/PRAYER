package com.example.prayer.model


data class Month(
    val name: String,
    var location :String,
    val days: List<Day>
)
data class Day(
        val date: String,
        val day_en: String,
        val times: Timings,
        var selected :Boolean,
        var today :Boolean
)
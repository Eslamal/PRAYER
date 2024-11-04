package com.example.prayer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prayer.api.RemoteDataSource
import com.example.prayer.model.Data
import com.example.prayer.model.Day
import com.example.prayer.model.Month
import com.example.prayer.model.PrayerData
import com.example.prayer.model.PrayerDatabase
import com.example.prayer.model.PrayerTimingDao
import com.example.prayer.model.PrayerTimingEntity
import com.example.prayer.model.Timings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PrayerHomeViewModel  : ViewModel() {
    private val _nextPrayer = MutableLiveData<String>()
    val nextPrayer: LiveData<String> get() = _nextPrayer

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String> get() = _timeLeft

    var prayerData = MutableLiveData<PrayerData?>()
    var apiRepository: RemoteDataSource = RemoteDataSource()
    var monthData = MutableLiveData<Month?>()

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("HANDLER", "CoroutineExceptionHandler got $exception")

    }

    fun getPrayerData(lat:String,lang: String,month:String,year:String) {
        CoroutineScope(Dispatchers.IO + handler).launch {
            val response = apiRepository.getPrayerTimes(lat,lang,month,year)
            prayerData.postValue(response)
            calculateNextPrayer(response)
        }
    }
    fun mapData(data : PrayerData){
        val days :MutableList<Day> = arrayListOf()
        val monthName = data.allData[0].date.gregorian.month.en
        val yearNum = data.allData[0].date.gregorian.year
        val location = data.allData[0].meta.timezone
        val name = monthName +" "+yearNum
        for (item in data.allData){
            val num = item.date.gregorian.day
            val day =item.date.gregorian.weekday.en
            val times = item.timings
            days.add(Day(num,day,times,false,false))
        }
        monthData.postValue(Month(name ,location,days))
    }
    private fun calculateNextPrayer(prayerTiming: PrayerData?) {
        prayerTiming?.let { data ->
            val timings = data.allData[0].timings
            val currentTime = Calendar.getInstance()
            val nextPrayerTime = getNextPrayerTime(timings, currentTime)

            if (nextPrayerTime != null) {
                _nextPrayer.postValue(nextPrayerTime.first) // Prayer name
                _timeLeft.postValue(nextPrayerTime.second) // Time left
            }
        }
    }

    private fun getNextPrayerTime(timings: Timings, currentTime: Calendar): Pair<String, String>? {
        val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        val prayerTimes = listOf(
            timings.Fajr,
            timings.Dhuhr,
            timings.Asr,
            timings.Maghrib,
            timings.Isha
        )

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault() // Adjusted to local time zone

        for (i in prayerNames.indices) {
            val prayerTime = dateFormat.parse(prayerTimes[i])
            if (prayerTime != null) {
                val prayerCalendar = Calendar.getInstance()
                prayerCalendar.time = prayerTime
                prayerCalendar.set(Calendar.YEAR, currentTime.get(Calendar.YEAR))
                prayerCalendar.set(Calendar.MONTH, currentTime.get(Calendar.MONTH))
                prayerCalendar.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH))

                if (prayerCalendar.after(currentTime)) {
                    val nextPrayerName = prayerNames[i]
                    val timeLeft = calculateTimeLeft(prayerCalendar.time)
                    return Pair(nextPrayerName, timeLeft)
                }
            }
        }

        return null // If no upcoming prayer
    }

    private fun calculateTimeLeft(prayerTime: Date): String {
        val currentTime = Calendar.getInstance().time
        val timeDiff = prayerTime.time - currentTime.time
        if (timeDiff < 0) return "No more prayers today" // Check if time difference is negative

        val hoursLeft = (timeDiff / (1000 * 60 * 60)).toInt()
        val minutesLeft = ((timeDiff / (1000 * 60)) % 60).toInt()
        return String.format("%02d:%02d", hoursLeft, minutesLeft)
    }

}


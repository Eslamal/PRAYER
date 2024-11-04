package com.example.prayer.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.prayer.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val actionBar = supportActionBar
        actionBar?.hide()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.islamic.visibility = View.VISIBLE
        binding.islamic.alpha=0f
        binding.islamic.animate().setDuration(1500).alpha(1f).withEndAction {
            binding.islamic.postDelayed({
                val intent = Intent(this, PrayerActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }
}
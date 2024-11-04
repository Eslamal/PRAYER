package com.example.prayer.view

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.prayer.databinding.ActivityQiblaBinding
import com.example.prayer.qibla.applyFullScreen


class QiblaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQiblaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        applyFullScreen()
        binding = ActivityQiblaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

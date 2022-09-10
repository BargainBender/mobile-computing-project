package com.example.midtermproject.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.midtermproject.R

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
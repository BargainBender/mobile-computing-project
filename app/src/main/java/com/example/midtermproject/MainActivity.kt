package com.example.midtermproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.midtermproject.aboutme.AboutMeActivity
import com.example.midtermproject.basicmath.BasicMathActivity
import com.example.midtermproject.firebasecrud.FirebaseCRUDActivity
import com.example.midtermproject.layoutdemo.LayoutDemoActivity
import com.example.midtermproject.stringmanipulation.StringManipulationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        findViewById<Button>(R.id.btnGotoBasicMath).setOnClickListener {
            startActivity(Intent(this, BasicMathActivity::class.java))
        }

        findViewById<Button>(R.id.btnGotoStringManipulation).setOnClickListener {
            startActivity(Intent(this, StringManipulationActivity::class.java))
        }

        findViewById<Button>(R.id.btnGotoLayoutDemo).setOnClickListener {
            startActivity(Intent(this, LayoutDemoActivity::class.java))
        }

        findViewById<Button>(R.id.btnGotoAboutMe).setOnClickListener {
            startActivity(Intent(this, AboutMeActivity::class.java))
        }

        findViewById<Button>(R.id.btnGotoTodo).setOnClickListener {
            startActivity(Intent(this, FirebaseCRUDActivity::class.java))
        }
    }
}
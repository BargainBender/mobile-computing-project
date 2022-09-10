package com.example.midtermproject.stringmanipulation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.midtermproject.R

class StringManipulationActivity : AppCompatActivity() {
    private lateinit var etStr: EditText
    private lateinit var btnGetAnswers: Button
    private lateinit var etQuestion1: EditText
    private lateinit var etQuestion2: EditText
    private lateinit var etQuestion3: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_manipulation)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        etStr = findViewById(R.id.etStr)
        btnGetAnswers = findViewById(R.id.btnGetAnswers)
        etQuestion1 = findViewById(R.id.etQuestion1)
        etQuestion2 = findViewById(R.id.etQuestion2)
        etQuestion3 = findViewById(R.id.etQuestion3)

        btnGetAnswers.setOnClickListener {
            etQuestion1.setText(StringManipulation.convertConsonantsToSign(etStr.text.toString()))
            etQuestion2.setText(StringManipulation.getUppercase(etStr.text.toString()))
            etQuestion3.setText(StringManipulation.getVowelCount(etStr.text.toString()).toString())
        }
    }
}
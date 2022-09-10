package com.example.midtermproject.basicmath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.midtermproject.R
import java.lang.Exception
import java.lang.NumberFormatException

class BasicMathActivity : AppCompatActivity() {
    private lateinit var etNum1: EditText
    private lateinit var etNum2: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView
    private lateinit var spinnerOperation: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_math)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        etNum1 = findViewById(R.id.etNum1)
        etNum2 = findViewById(R.id.etNum2)
        tvResult = findViewById(R.id.tvResult)
        btnCalculate = findViewById(R.id.btnCalculate)
        spinnerOperation = findViewById(R.id.spinnerOperation)

        btnCalculate.setOnClickListener {
            try {
                val num1 = etNum1.text.toString().toInt()
                val num2 = etNum2.text.toString().toInt()

                when (spinnerOperation.selectedItem) {
                    "Addition" -> tvResult.text = BasicMathFunctions.add(num1, num2).toString();
                    "Subtraction" -> tvResult.text = BasicMathFunctions.subtract(num1, num2).toString()
                    "Multiplication" -> tvResult.text = BasicMathFunctions.multiply(num1, num2).toString()
                    "Division" -> tvResult.text = BasicMathFunctions.divide(num1, num2).toString()
                    else -> Toast.makeText(applicationContext, "No such operation found.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                val message = if (e is NumberFormatException) {
                    "An error occurred: Invalid input"
                } else {
                    "An error occurred: ${e.message}"
                }
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                Log.println(Log.ERROR, "Calculation", e.toString())
            }
        }
    }
}

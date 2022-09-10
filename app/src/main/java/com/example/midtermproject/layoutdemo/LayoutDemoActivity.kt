
package com.example.midtermproject.layoutdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.midtermproject.R

class LayoutDemoActivity : AppCompatActivity() {
    private lateinit var btnText: Button
    private lateinit var btnImageTextButton: Button
    private lateinit var ibtnImage: ImageButton
    private lateinit var cbxFruit: CheckBox
    private lateinit var cbxMeat: CheckBox
    private lateinit var rgrpLevel: RadioGroup
    private lateinit var tbtnPower: ToggleButton
    private lateinit var ratingBar: RatingBar
    private lateinit var etxtMsg: EditText
    private lateinit var btnToast: Button

    private var checkBoxValue = ""
    private var radioButtonValue = ""
    private var toggleButtonValue = "Off"
    private var ratingBarValue = "0.0"
    private var editTextValue = ""

    private var fruit = false
    private var meat = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_demo)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        btnText = findViewById(R.id.layout_demo_act_btn_text)
        btnImageTextButton = findViewById(R.id.layout_demo_act_btn_text_image)
        ibtnImage = findViewById(R.id.layout_demo_act_ibtn_image)
        cbxFruit = findViewById(R.id.layout_demo_act_cbx_fruit)
        cbxMeat = findViewById(R.id.layout_demo_act_cbx_meat)
        rgrpLevel = findViewById(R.id.layout_demo_act_rgrp_level)
        tbtnPower = findViewById(R.id.layout_demo_act_tbtn_power)
        ratingBar = findViewById(R.id.layout_demo_act_rbar_star)
        etxtMsg = findViewById(R.id.layout_demo_act_etxt_msg)
        btnToast = findViewById(R.id.layout_demo_act_btn_toast)

        cbxFruit.setOnClickListener { view -> doCheckBox(view) }
        cbxMeat.setOnClickListener { view -> doCheckBox(view) }
        rgrpLevel.setOnCheckedChangeListener{ _, i -> run {
            when (i) {
                R.id.layout_demo_act_rbtn_one -> radioButtonValue = "One"
                R.id.layout_demo_act_rbtn_two -> radioButtonValue = "Two"
                R.id.layout_demo_act_rbtn_three -> radioButtonValue = "Three"
            }
            println("Radio Button Value: $radioButtonValue")
        }}

        btnText.setOnClickListener { doShortToast("You Clicked Text Button") }
        btnImageTextButton.setOnClickListener { doShortToast("You Clicked Image Text Button") }
        ibtnImage.setOnClickListener{ doShortToast("You Clicked Image Button") }

        tbtnPower.setOnCheckedChangeListener { _, checked -> run {
            toggleButtonValue = if (checked) "On" else "Off"
            println("Toggle Button Value: $toggleButtonValue")
        }}

        ratingBar.setOnRatingBarChangeListener { _, rating, _ -> run {
            ratingBarValue = rating.toString()
            println("Rating Bar Value: $ratingBarValue")
        }}

        btnToast.setOnClickListener { doCollectValues() }
    }

    private fun doCheckBox(view: View?) {
        if (view is CheckBox) {
            val checked = view.isChecked
            when (view.id) {
                R.id.layout_demo_act_cbx_fruit -> fruit = checked
                R.id.layout_demo_act_cbx_meat -> meat = checked
            }

            checkBoxValue = if (fruit && meat) {
                "Fruit and Meat"
            } else if (fruit) {
                "Fruit"
            } else if (meat) {
                "Meat"
            } else {
                "No Selection"
            }

            println("Check Boxes: $checkBoxValue")
        }
    }

    private fun doCollectValues() {
        var message = ""
        editTextValue = etxtMsg.text.toString()
        message += "Check Box: $checkBoxValue\n"
        message += "Radio Button: $radioButtonValue\n"
        message += "Toggle Button: $toggleButtonValue\n"
        message += "Rating Bar: $ratingBarValue\n"
        message += "Edit Text: $editTextValue"
        doLongToast(message)
    }

    private fun doShortToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun doLongToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}
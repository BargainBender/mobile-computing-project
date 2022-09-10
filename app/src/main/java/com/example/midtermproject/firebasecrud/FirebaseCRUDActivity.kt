package com.example.midtermproject.firebasecrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.midtermproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

private const val DEFAULT_LIST: Int = 0
class FirebaseCRUDActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fabFirebaseCreate: FloatingActionButton
    private lateinit var fabFirebaseCreateList: FloatingActionButton
    private lateinit var fabFirebaseCreateTodo: FloatingActionButton

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var fabMenuOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_crud)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        fabFirebaseCreate = findViewById(R.id.fabFirebaseCreate)
        fabFirebaseCreateList = findViewById(R.id.fabFirebaseCreateList)
        fabFirebaseCreateTodo = findViewById(R.id.fabFirebaseCreateTodo)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        var navMenu = findViewById<NavigationView>(R.id.nav_menu)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        // Checks if navbar indicator is open / closed
        toggle.syncState()

        fabFirebaseCreate.setOnClickListener { onFirebaseCreateBtnClicked() }
        fabFirebaseCreateList.setOnClickListener {
            Toast.makeText(this, "FAB clicked!", Toast.LENGTH_SHORT).show()
        }
        fabFirebaseCreateTodo.setOnClickListener {
            Toast.makeText(this, "FAB clicked!", Toast.LENGTH_SHORT).show()
        }

        DB.getTodoLists(object: DataCollectedCallback {
            override fun callFunction(value: List<String>) {
                value.forEach { title ->
                    Log.d("TODOS", title)
                    navMenu.animation
                    navMenu.menu.add(Menu.NONE, DEFAULT_LIST, Menu.NONE, title)
                }
            }
        })
    }

    private fun onFirebaseCreateBtnClicked() {
        setVisibility(fabMenuOpened)
        setAnimation(fabMenuOpened)
        fabMenuOpened = !fabMenuOpened
    }

    private fun setVisibility(opened: Boolean) {
        if (!opened) {
            fabFirebaseCreateList.visibility = View.VISIBLE
            fabFirebaseCreateTodo.visibility = View.VISIBLE
        } else {
            fabFirebaseCreateList.visibility = View.GONE
            fabFirebaseCreateTodo.visibility = View.GONE
        }
    }

    private fun setAnimation(opened: Boolean) {
        if (!opened) {
            fabFirebaseCreateList.startAnimation(fromBottom)
            fabFirebaseCreateTodo.startAnimation(fromBottom)
            fabFirebaseCreate.startAnimation(rotateOpen)
        } else {
            fabFirebaseCreateList.startAnimation(toBottom)
            fabFirebaseCreateTodo.startAnimation(toBottom)
            fabFirebaseCreate.startAnimation(rotateClose)
        }
    }
}
package com.example.midtermproject.firebasecrud

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midtermproject.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

private const val DEFAULT_LIST: Int = 0
class FirebaseCRUDActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fabFirebaseCreate: FloatingActionButton
    private lateinit var fabFirebaseCreateList: FloatingActionButton
    private lateinit var fabFirebaseCreateTodo: FloatingActionButton
    private lateinit var todoRecyclerView: RecyclerView

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var fabMenuOpened = false

    private var openedList = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_crud)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        fabFirebaseCreate = findViewById(R.id.fabFirebaseCreate)
        fabFirebaseCreateList = findViewById(R.id.fabFirebaseCreateList)
        fabFirebaseCreateTodo = findViewById(R.id.fabFirebaseCreateTodo)
        todoRecyclerView = findViewById(R.id.rvTodos)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navMenu = findViewById<NavigationView>(R.id.nav_menu)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        // Checks if navbar indicator is open / closed
        toggle.syncState()

        fabFirebaseCreate.setOnClickListener { onFirebaseCreateBtnClicked() }
        fabFirebaseCreateList.setOnClickListener {
            Toast.makeText(this, "FAB New To-do clicked", Toast.LENGTH_SHORT).show()
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            val etInput = EditText(this)
            alert.setTitle("To-do:")
            alert.setView(etInput)
            alert.setPositiveButton("Add") { _, _ ->
                val etValue = etInput.text.toString()
                Log.d("DIALOG", "New To-do: $etValue")
            }

            alert.setNegativeButton("Cancel") { _, _ ->
                Log.d("DIALOG", "Cancelled")
            }

            alert.show()
        }
        fabFirebaseCreateTodo.setOnClickListener {
            Toast.makeText(this, "FAB New To-do list clicked!", Toast.LENGTH_SHORT).show()
        }

        DB.getTodoLists(object: TodoListsCollectedCallback {
            override fun callFunction(value: HashMap<String, TodoListModel>) {
                value.forEach {
                    Log.d("TODOS", "${it.key} -> ${it.value.getTitle()}")
                    // Set action for clicking each menu item
                    navMenu.menu.add(Menu.NONE, DEFAULT_LIST, Menu.NONE, it.value.getTitle()).setOnMenuItemClickListener { menuItem ->
                        Toast.makeText(applicationContext, it.value.getTitle(),Toast.LENGTH_SHORT).show()
                        Log.d("TODOS-TITLE", menuItem.title.toString())
                        Log.d("TODOS-LIST-ID", it.key)
                        drawerLayout.closeDrawer(GravityCompat.START)

                        loadTodos(it.key)
                        openedList = it.key
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        })

        loadTodos("default")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val currentEt = currentFocus
                if (currentEt is EditText) {
                    val outRect = Rect()
                    currentEt.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentEt.windowToken, 0)
                        // All this just to clear focus on touch outside of currently focused edittext
                        currentEt.clearFocus()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun loadTodos(list: String) {
        DB.getTodos(list, object: TodosCollectedCallback {
            override fun callFunction(value: HashMap<String, TodoModel>) {
                value.forEach {
                    Log.d("TODOS-${list}", "${it.key} -> ${it.value.getIndex()}: ${it.value.getTitle()}, ${if(it.value.getIsDone()) "Done" else "Not done"}")
                }
                val todoRVAdapter = TodoRecyclerViewAdapter(this@FirebaseCRUDActivity, value)
                todoRecyclerView.adapter = todoRVAdapter
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
package com.example.midtermproject.firebasecrud

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
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
    private lateinit var navMenu: NavigationView
    private lateinit var tvEmptyMessage: TextView

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var fabMenuOpened = false

    companion object {
        var openedList = "default"
        var positionIDMap = hashMapOf<Int, String>()
    }

    private var openedListMaxIndex = 0
    private var listsMaxIndex = 0
    private var currentList: HashMap<String, TodoModel> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_crud)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)
        fabFirebaseCreate = findViewById(R.id.fabFirebaseCreate)
        fabFirebaseCreateList = findViewById(R.id.fabFirebaseCreateList)
        fabFirebaseCreateTodo = findViewById(R.id.fabFirebaseCreateTodo)
        todoRecyclerView = findViewById(R.id.rvTodos)
        todoRecyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        navMenu = findViewById(R.id.nav_menu)
        drawerLayout = findViewById(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        // Checks if navbar indicator is open / closed
        toggle.syncState()

        fabFirebaseCreate.setOnClickListener { onFirebaseCreateBtnClicked() }
        fabFirebaseCreateList.setOnClickListener { createTodoOrList(it) }
        fabFirebaseCreateTodo.setOnClickListener { createTodoOrList(it) }

        loadTodoLists()

        loadTodos("default")
    }

    private fun loadTodoLists() {
        DB.getTodoLists(object: TodoListsCollectedCallback {
            override fun callFunction(value: HashMap<String, TodoListModel>) {
                navMenu.menu.removeGroup(Menu.CATEGORY_CONTAINER)
                value.forEach {
                    Log.d("TODOS", "${it.key} -> ${it.value.getTitle()}")
                    // Set action for clicking each menu item
                    navMenu.menu.add(Menu.CATEGORY_CONTAINER, DEFAULT_LIST, Menu.NONE, it.value.getTitle()).setOnMenuItemClickListener { menuItem ->
                        Toast.makeText(applicationContext, it.value.getTitle(),Toast.LENGTH_SHORT).show()
                        Log.d("TODOS-TITLE", menuItem.title.toString())
                        Log.d("TODOS-LIST-ID", it.key)
                        drawerLayout.closeDrawer(GravityCompat.START)

                        loadTodos(it.key)
                        openedList = it.key
                        Log.d("MAX-INDEX-LISTS", listsMaxIndex.toString())
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        })
        DB.getTodoMaxIndex(openedList, object : GetMaxIndexCallback {
            override fun callFunction(maxIndex: Int) {
                listsMaxIndex = maxIndex
            }
        })
    }

    private fun createTodoOrList(view: View?) {
        val etInput = EditText(this)
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)
        var title = ""
        if (view is FloatingActionButton) {
            when (view.id) {
                R.id.fabFirebaseCreateTodo -> {
                    title = "Add to-do"
                    alert.setPositiveButton("Add") { _, _ ->
                        val etValue = etInput.text.toString()
                        DB.createTodo(openedList, etValue, openedListMaxIndex + 1, object : CreateTodoOrListCallback {
                            override fun callFunction(id: String) {
                                loadTodos(openedList)
                                Log.d("DIALOG", "New To-do: $id -> $etValue")
                            }
                        })
                    }
                }
                R.id.fabFirebaseCreateList -> {
                    title = "Create to-do list"
                    alert.setPositiveButton("Create") { _, _ ->
                        val etValue = etInput.text.toString()
                        DB.createTodoList(etValue, listsMaxIndex, object : CreateTodoOrListCallback {
                            override fun callFunction(id: String) {
                                loadTodoLists()
                                Log.d("DIALOG", "New To-do list: $id -> $etValue")
                            }
                        })
                    }
                }
            }
        }

        alert.setView(etInput)
        alert.setTitle(title)

        alert.setNegativeButton("Cancel") { _, _ ->
            Log.d("DIALOG", "Cancelled")
        }

        alert.show()
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
                if (value.size > 0) {
                    tvEmptyMessage.visibility = View.GONE
                } else {
                    tvEmptyMessage.visibility = View.VISIBLE
                }
                value.forEach {
                    Log.d("TODOS-${list}", "${it.key} -> ${it.value.getIndex()}: ${it.value.getTitle()}, ${if(it.value.getIsDone()) "Done" else "Not done"}")
                }

                currentList = value
                val todoRVAdapter = TodoRecyclerViewAdapter(this@FirebaseCRUDActivity, currentList)
                todoRecyclerView.adapter = todoRVAdapter

                val swipeDeleteCallback = object : SwipeDeleteCallback() {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition

                        DB.deleteTodo(openedList, positionIDMap[position].toString())

                        Log.d("REMOVE", "onSwiped: ${positionIDMap[position]}")
                        Log.d("REMOVE", "onSwiped: ${currentList[positionIDMap[position]]!!.getTitle()}")
                        currentList.remove(positionIDMap[position])
                        if (currentList.size < 1) {
                            tvEmptyMessage.visibility = View.VISIBLE
                        } else {
                            tvEmptyMessage.visibility = View.GONE
                        }
                        // Set a new adapter for the new list
                        val newTodoRVAdapter = TodoRecyclerViewAdapter(this@FirebaseCRUDActivity, currentList)
                        todoRecyclerView.adapter = newTodoRVAdapter
                        newTodoRVAdapter.notifyItemRemoved(position)
                    }
                }

                val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
                itemTouchHelper.attachToRecyclerView(todoRecyclerView)

                openedListMaxIndex = value.size - 1
                Log.d("MAX-INDEX", openedListMaxIndex.toString())
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
package com.example.midtermproject.firebasecrud

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.midtermproject.R

class TodoRecyclerViewAdapter(_context: Context, _todos: HashMap<String, TodoModel>) : RecyclerView.Adapter<TodoRecyclerViewAdapter.TodoViewHolder>() {
    var context: Context = _context
    private var todos: List<Pair<String, TodoModel>> = _todos.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // Inflate the layout, give look to rows
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.rw_todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        // Assign values to each rows based on the position of the recycler view
        holder.etTitle.setText(todos[position].second.getTitle())
        holder.cbIsDone.isChecked = todos[position].second.getIsDone()
        holder.cbIsDone.setOnCheckedChangeListener { _, b ->
            // Strikethrough depending on check value
            if (b) {
                holder.etTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.etTitle.paintFlags = 0
            }
        }
        // Strikethrough depending on check value
        if (todos[position].second.getIsDone()) {
            holder.etTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    override fun getItemCount(): Int {
        // self explanatory
        return this.todos.size
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Similar to onCreate, grab all views on rw_todo_item
        var cbIsDone: CheckBox
        var etTitle: EditText

        init {
            cbIsDone = itemView.findViewById(R.id.cbTodo)
            etTitle = itemView.findViewById(R.id.etTodo)
        }
    }
}
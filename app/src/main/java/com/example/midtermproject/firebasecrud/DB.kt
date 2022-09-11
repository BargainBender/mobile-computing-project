package com.example.midtermproject.firebasecrud

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DB {
    private val db = Firebase.firestore

    fun getTodoLists(callback: TodoListsCollectedCallback) {
        val lists = hashMapOf<String, TodoListModel>()
        db.collection("todo-lists")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        lists[document.id] = TodoListModel(Integer.parseInt(document.data["index"].toString()), document.data["title"].toString())
                    }
                }
                val result = lists.toList().sortedBy { (_, value) ->  value.getIndex() }.toMap()
                callback.callFunction(result as HashMap<String, TodoListModel>)
            }
    }

    fun getTodos(list: String, callback: TodosCollectedCallback) {
        val todos = hashMapOf<String, TodoModel>()
        db.collection("todo-lists/${list}/todos")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        todos[document.id] = TodoModel(Integer.parseInt(document.data["index"].toString()), document.data["todo"].toString(), document.data["isDone"].toString().toBoolean())
                    }
                }
                // Sort by index
                val result = todos.toList().sortedBy { (_, value) -> value.getIndex() }.toMap()
                callback.callFunction(result as HashMap<String, TodoModel>)
            }
    }
}

// Create our functions on the fly with this parameter
interface TodoListsCollectedCallback {
    fun callFunction(value: HashMap<String, TodoListModel>)
}

interface TodosCollectedCallback {
    fun callFunction(value: HashMap<String, TodoModel>)
}

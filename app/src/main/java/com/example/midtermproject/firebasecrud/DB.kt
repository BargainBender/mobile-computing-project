package com.example.midtermproject.firebasecrud

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DB {
    private val db = Firebase.firestore

    fun getTodoLists(callback: TodoListsCollectedCallback) {
        val lists = hashMapOf<String, TodoListModel>()
        db.collection("todo-lists")
            // Currently not working
            // .orderBy("index")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        lists[document.id] = TodoListModel(Integer.parseInt(document.data["index"].toString()), document.data["title"].toString())
                    }
                }
                if (lists.size > 1) {
                    val result = lists.toList().sortedBy { (_, value) ->  value.getIndex() }.toMap()
                    callback.callFunction(result as HashMap<String, TodoListModel>)
                } else {
                    callback.callFunction(lists)
                }
            }
    }

    fun getTodos(list: String, callback: TodosCollectedCallback) {
        val todos = hashMapOf<String, TodoModel>()
        db.collection("todo-lists/$list/todos")
            // Currently not working
            //.orderBy("index", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        todos[document.id] = TodoModel(Integer.parseInt(document.data["index"].toString()), document.data["todo"].toString(), document.data["isDone"].toString().toBoolean())
                    }
                }
                // Sort by index
                if (todos.size > 1) {
                    val result = todos.toList().sortedBy { (_, value) ->  value.getIndex() }.toMap()
                    callback.callFunction(result as HashMap<String, TodoModel>)
                } else {
                    callback.callFunction(todos)
                }
            }
    }

    fun createTodo(list: String, title: String, index: Int, callback: CreateTodoOrListCallback) {
        val todo = hashMapOf(
            "index" to index,
            "isDone" to false,
            "todo" to title
        )
        db.collection("todo-lists/$list/todos")
            .add(todo)
            .addOnCompleteListener {
                callback.callFunction(it.result.id)
            }

        db.document("todo-lists/$list").update(mapOf("maxIndex" to FieldValue.increment(1)))
    }

    fun getTodoMaxIndex(list: String, callback: GetMaxIndexCallback) {
        var maxIndex = 0
        db.document("todo-lists/$list")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    maxIndex = Integer.parseInt(task.result.data!!["maxIndex"].toString())
                }
            }
        callback.callFunction(maxIndex)
    }

    fun updateTodoTitle(title: String, list: String, id: String) {
        db.collection("todo-lists/$list/todos")
            .document(id)
            .update(mapOf("todo" to title))
            .addOnCompleteListener{
                Log.d("UPDATE","Update at todo-lists/$list/todos/$id")
            }
    }

    fun createTodoList(title: String, index: Int, callback: CreateTodoOrListCallback) {
        val todo = hashMapOf(
            "index" to index,
            "title" to title,
            "maxIndex" to 0
        )
        db.collection("todo-lists")
            .add(todo)
            .addOnCompleteListener {
                callback.callFunction(it.result.id)
            }
    }

    fun deleteTodo(list: String, id: String) {
        db.document("todo-lists/$list/todos/$id")
            .delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("DELETE", "deleteTodo: Success")
                }
            }
    }

    fun setTodoCheck(isDone: Boolean, list: String, id: String) {
        db.collection("todo-lists/$list/todos")
            .document(id)
            .update(mapOf("isDone" to isDone))
            .addOnCompleteListener{
                Log.d("UPDATE","Update at todo-lists/$list/todos/$id")
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

interface CreateTodoOrListCallback {
    fun callFunction(id: String)
}

interface GetMaxIndexCallback{
    fun callFunction(maxIndex: Int)
}

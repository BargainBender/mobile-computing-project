package com.example.midtermproject.firebasecrud

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DB {
    private val db = Firebase.firestore

    fun getTodoLists(callback: DataCollectedCallback) {
        var lists = mutableListOf<String>()
        Log.d("RESULT", "Loading result:")
        db.collection("todo-lists")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        lists.add(document.data["title"].toString())
                    }
                }
                callback.callFunction(lists)
            }
    }
}

// Create our functions on the fly with this parameter
interface DataCollectedCallback {
    fun callFunction(value: List<String>)
}
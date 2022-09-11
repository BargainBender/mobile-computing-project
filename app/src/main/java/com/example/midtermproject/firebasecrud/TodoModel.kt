package com.example.midtermproject.firebasecrud

class TodoModel constructor(_index: Int, _title: String, _isDone: Boolean = false) {
    private var index = _index
    private var title = _title
    private var isDone = _isDone

    fun getIndex(): Int {
        return this.index
    }

    fun setIndex(ind: Int) {
        this.index = ind
    }

    fun getTitle(): String {
        return this.title
    }

    fun setTitle(ti: String) {
        this.title = ti
    }

    fun getIsDone(): Boolean {
        return this.isDone
    }

    fun setIsDone(done: Boolean) {
        this.isDone = done
    }
}
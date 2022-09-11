package com.example.midtermproject.firebasecrud

class TodoListModel constructor(_index: Int, _title: String) {
    private var index = _index
    private var title = _title

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
}

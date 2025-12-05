package com.example.kotlinrecyclerviewapp

interface MainContract {

    interface View {
        fun showData(data: List<Pair<Data, Boolean>>)
        fun showToast(message: String)
        fun scrollToBottom()
    }

    interface Presenter {
        fun onViewCreated()
        fun onItemClick(data: Data)
        fun onFabClick()
        fun onItemMoved(fromPosition: Int, toPosition: Int)
        fun onItemDismissed(position: Int)
    }
}
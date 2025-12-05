package com.example.kotlinrecyclerviewapp

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private val data = mutableListOf<Pair<Data, Boolean>>()

    override fun onViewCreated() {
        initializeData()
        view.showData(data)
    }

    private fun initializeData() {
        data.clear()
        data.addAll(
            listOf(
                Pair(Data("Earth"), false),
                Pair(Data("Earth"), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Earth"), false),
                Pair(Data("Earth"), false),
                Pair(Data("Earth"), false),
                Pair(Data("Mars", null), false)
            )
        )
        data.add(0, Pair(Data("Header"), false))
    }

    override fun onItemClick(data: Data) {
        view.showToast(data.someText)
    }

    override fun onFabClick() {
        data.add(Pair(Data("Mars", ""), false))
        view.showData(data)
        view.scrollToBottom()
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        view.showData(data)
    }

    override fun onItemDismissed(position: Int) {
        data.removeAt(position)
        view.showData(data)
    }
}
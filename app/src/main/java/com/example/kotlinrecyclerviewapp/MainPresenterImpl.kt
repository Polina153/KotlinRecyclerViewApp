package com.example.kotlinrecyclerviewapp

import android.util.Log

class MainPresenterImpl(private val view: MainContract.View) : MainContract.Presenter {

    private val TAG = "MainPresenterImpl"

    // Модель данных — хранилище состояния
    private val dataModel = mutableListOf<Pair<Data, Boolean>>()

    override fun onViewCreated() {
        Log.d(TAG, "onViewCreated() вызван")
        initializeData()
        view.showData(dataModel)
    }

    private fun initializeData() {
        dataModel.clear()
        dataModel.addAll(
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
        dataModel.add(0, Pair(Data("Header"), false))
        Log.d(TAG, "Инициализировано ${dataModel.size} элементов")
    }

    override fun onItemClick(data: Data) {
        Log.d(TAG, "Клик по элементу: ${data.someText}")
        view.showToast(data.someText)
    }

    override fun onFabClick() {
        Log.d(TAG, "Клик по FAB — добавляем новый элемент")
        val newItem = Pair(Data("Mars", ""), false)
        dataModel.add(newItem)
        view.showData(dataModel)
        view.scrollToBottom()
        Log.d(TAG, "Добавлен новый элемент. Всего: ${dataModel.size}")
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Log.d(TAG, "Перемещение элемента: $fromPosition -> $toPosition")
        val movedItem = dataModel.removeAt(fromPosition)
        dataModel.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, movedItem)
        view.showData(dataModel)
    }

    override fun onItemDismissed(position: Int) {
        Log.d(TAG, "Удаление элемента на позиции: $position")
        dataModel.removeAt(position)
        view.showData(dataModel)
    }

    // Дополнительные методы для расширения функционала
    fun toggleItemText(position: Int) {
        if (position in dataModel.indices) {
            val currentItem = dataModel[position]
            dataModel[position] = Pair(currentItem.first, !currentItem.second)
            view.showData(dataModel)
        }
    }

    fun getDataSize(): Int = dataModel.size

    fun clearAllData() {
        dataModel.clear()
        view.showData(dataModel)
    }
}
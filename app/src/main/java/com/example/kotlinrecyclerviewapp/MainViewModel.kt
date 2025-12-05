package com.example.kotlinrecyclerviewapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _dataList = MutableLiveData<MutableList<Pair<RecyclerData, Boolean>>>()
    val dataList: LiveData<MutableList<Pair<RecyclerData, Boolean>>> = _dataList

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    init {
        initializeData()
    }

    private fun initializeData() {
        val initialData = mutableListOf(
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Mars", ""), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Earth"), false),
            Pair(RecyclerData("Mars", null), false)
        )
        initialData.add(0, Pair(RecyclerData("Header"), false))
        _dataList.value = initialData
    }

    fun onItemClick(data: RecyclerData) {
        _toastMessage.value = data.someText
    }

    fun onFabClick() {
        val currentData = _dataList.value ?: mutableListOf()
        currentData.add(Pair(RecyclerData("Mars", ""), false))
        _dataList.value = currentData
    }

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val currentData = _dataList.value ?: return
        val movedItem = currentData.removeAt(fromPosition)
        currentData.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, movedItem)
        _dataList.value = ArrayList(currentData) // ✅ Создаем копию для триггера LiveData
    }

    fun onItemDismissed(position: Int) {
        val currentData = _dataList.value ?: return
        currentData.removeAt(position)
        _dataList.value = ArrayList(currentData)
    }

    fun toggleItemText(position: Int) {
        val currentData = _dataList.value ?: return
        if (position in currentData.indices) {
            val currentItem = currentData[position]
            currentData[position] = Pair(currentItem.first, !currentItem.second)
            _dataList.value = ArrayList(currentData)
        }
    }
}
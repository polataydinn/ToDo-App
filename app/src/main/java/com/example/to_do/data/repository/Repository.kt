package com.example.to_do.data.repository

import androidx.lifecycle.LiveData
import com.example.to_do.data.ToDoDao
import com.example.to_do.data.models.ToDoData

class Repository(private val toDoDao: ToDoDao) {

    val getAllData = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData){
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String) = toDoDao.searchDatabase(searchQuery)

    fun sortByHighPriority() = toDoDao.sortByHighPriority()

    fun sortByLowPriority() = toDoDao.sortByLowPriority()
}
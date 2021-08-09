package com.example.to_do.data.repository

import com.example.to_do.data.ToDoDao
import com.example.to_do.data.models.ToDoData

class Repository(private val toDoDao: ToDoDao) {

    val getAllData = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }
}
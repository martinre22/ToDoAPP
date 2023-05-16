package com.example.todoapp.addtasks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("select * from task")
    suspend fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(item: TaskEntity)
}

package com.example.dailytask.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithUser (
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val task: List<Task>
){

}
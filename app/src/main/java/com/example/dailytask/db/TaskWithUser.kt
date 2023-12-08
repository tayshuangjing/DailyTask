package com.example.dailytask.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithUser (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val user: User
)